package com.uyibai.gateway.suports.dubbo;

import static java.util.Collections.emptyMap;
import static org.apache.dubbo.common.constants.CommonConstants.GROUP_KEY;
import static org.apache.dubbo.common.constants.CommonConstants.VERSION_KEY;
import static org.springframework.util.StringUtils.commaDelimitedListToStringArray;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;

import com.alibaba.cloud.dubbo.metadata.DubboRestServiceMetadata;
import com.alibaba.cloud.dubbo.metadata.ServiceRestMetadata;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceFactory;
import com.uyibai.gateway.suports.dubbo.vo.DubboRegister;
import com.uyibai.gateway.suports.dubbo.vo.DubboServiceMeta;

/**
 * GenericServiceFactory 默认实现只支持 dubbo rest 的泛化调用, 为了 支持 普通的 dubbo rpc 泛化调用,
 * GenericServiceFactory 中 有 ReferenceBean<GenericService> cache 为了 保证 ReferenceBean cache 只在一个 地方管理和释放,
 * 以及泛化接口工厂统一管理
 * 通过此类 来替换 GenericServiceFactory
 */
@Component
public class GatewayDubboGenericServiceFactory extends DubboGenericServiceFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConcurrentMap<String, ReferenceBean<GenericService>> cache = new ConcurrentHashMap<>();

    /**
     *
     */
    @Autowired
    private ObjectProvider<List<RegistryConfig>> registryConfigs;

    private final ConcurrentMap<String, RegistryConfig> customRegistryConfigs = new ConcurrentHashMap<>();

    @Override
    public GenericService create(DubboRestServiceMetadata dubboServiceMetadata,
                                 Map<String, Object> dubboTranslatedAttributes) {

        ReferenceBean<GenericService> referenceBean = build(
                dubboServiceMetadata.getServiceRestMetadata(), dubboTranslatedAttributes);

        return referenceBean == null ? null : referenceBean.get();
    }

    public ConcurrentMap<String, ReferenceBean<GenericService>> getCache() {
        return cache;
    }

    @Override
    public GenericService create(String serviceName, Class<?> serviceClass,
                                 String version) {
        String interfaceName = serviceClass.getName();
        ReferenceBean<GenericService> referenceBean = build(interfaceName, version,
                serviceName, emptyMap());
        return referenceBean.get();
    }

    public GenericService create(DubboServiceMeta serviceMeta) {
        ReferenceBean<GenericService> referenceBean = build(serviceMeta);
        return referenceBean.get();
    }


    private ReferenceBean<GenericService> build(ServiceRestMetadata serviceRestMetadata,
                                                Map<String, Object> dubboTranslatedAttributes) {
        String urlValue = serviceRestMetadata.getUrl();
        URL url = URL.valueOf(urlValue);
        String interfaceName = url.getServiceInterface();
        String version = url.getParameter(VERSION_KEY);
        String group = url.getParameter(GROUP_KEY);

        return build(interfaceName, version, group, dubboTranslatedAttributes);
    }

    /**
     * 构建  ReferenceBean<GenericService>
     *
     * @param interfaceName             接口名称
     * @param version                   版本
     * @param group                     分组
     * @param dubboTranslatedAttributes
     * @return
     */
    private ReferenceBean<GenericService> build(String interfaceName, String version,
                                                String group, Map<String, Object> dubboTranslatedAttributes) {

        String key = createKey(interfaceName, version, group, dubboTranslatedAttributes);

        return cache.computeIfAbsent(key, k -> {
            ReferenceBean<GenericService> referenceBean = new ReferenceBean<>();
            // 泛化类型： org.apache.dubbo.common.constants.CommonConstants
            /*
             String GENERIC_SERIALIZATION_NATIVE_JAVA = "nativejava";
             String GENERIC_SERIALIZATION_DEFAULT = "true";
             String GENERIC_SERIALIZATION_BEAN = "bean";
             String GENERIC_RAW_RETURN = "raw.return";
             String GENERIC_SERIALIZATION_PROTOBUF = "protobuf-json";
             */
            referenceBean.setGeneric("true");
            referenceBean.setInterface(interfaceName);
            referenceBean.setVersion(version);
            referenceBean.setGroup(group);
            referenceBean.setCheck(false);
            registryConfigs.ifAvailable(referenceBean::setRegistries);
            if (CollectionUtils.isNotEmptyMap(dubboTranslatedAttributes)) {
                bindReferenceBean(referenceBean, dubboTranslatedAttributes);
            }
            return referenceBean;
        });
    }

    /**
     * 根据 dubboServiceMeta 构建 referenceBean
     *
     * @param serviceMeta meta信息
     * @return
     */
    private ReferenceBean<GenericService> build(DubboServiceMeta serviceMeta) {
        String interfaceName = serviceMeta.getServiceInterface();
        String version = serviceMeta.getVersion();
        String group = serviceMeta.getGroup();
        String key = createKey(interfaceName, version, group, emptyMap());
        return cache.computeIfAbsent(key, k -> {
            ReferenceBean<GenericService> referenceBean = new ReferenceBean<>();
            referenceBean.setGeneric("true");
            referenceBean.setInterface(interfaceName);
            referenceBean.setVersion(version);
            referenceBean.setGroup(group);
            referenceBean.setCheck(false);
            referenceBean.setRegistries(getRegistryList(serviceMeta.getRegistrys()));
            if (CollectionUtils.isNotEmptyMap(serviceMeta.getTranslatedAttrs())) {
                bindReferenceBean(referenceBean, serviceMeta.getTranslatedAttrs());
            }
            return referenceBean;
        });
    }

    /**
     * 构建 注册中心 配置信息
     *
     * @param registers 注册中心信息
     * @return
     */
    private List<RegistryConfig> getRegistryList(List<DubboRegister> registers) {
        return registers.stream().map(item -> customRegistryConfigs.computeIfAbsent(item.getRegistryKey(), kk -> {
            RegistryConfig config = new RegistryConfig();
            config.setId(item.getRegistryKey());
            config.setAddress(item.getAddress());
            if (StringUtils.hasText(item.getGroup())) {
                config.setGroup(item.getGroup());
            }
            if (CollectionUtils.isNotEmptyMap(item.getParameters())) {
                config.setParameters(item.getParameters());
            }
            if (item.getTimeout() != null) {
                config.setTimeout(item.getTimeout());
            }
            if (StringUtils.hasText(item.getUsername())) {
                config.setUsername(item.getUsername());
            }
            if (StringUtils.hasText(item.getPassword())) {
                config.setUsername(item.getPassword());
            }
            return config;
        })).collect(Collectors.toList());
    }

    private String createKey(String interfaceName, String version, String group,
                             Map<String, Object> dubboTranslatedAttributes) {
        return group + "#"
                + Objects.hash(interfaceName, version, group, dubboTranslatedAttributes);
    }

    private void bindReferenceBean(ReferenceBean<GenericService> referenceBean,
                                   Map<String, Object> dubboTranslatedAttributes) {
        DataBinder dataBinder = new DataBinder(referenceBean);
        // Register CustomEditors for special fields
        dataBinder.registerCustomEditor(String.class, "filter",
                new StringTrimmerEditor(true));
        dataBinder.registerCustomEditor(String.class, "listener",
                new StringTrimmerEditor(true));
        dataBinder.registerCustomEditor(Map.class, "parameters",
                new PropertyEditorSupport() {

                    @Override
                    public void setAsText(String text)
                            throws IllegalArgumentException {
                        // Trim all whitespace
                        String content = StringUtils.trimAllWhitespace(text);
                        if (!StringUtils.hasText(content)) { // No content , ignore
                            // directly
                            return;
                        }
                        // replace "=" to ","
                        content = StringUtils.replace(content, "=", ",");
                        // replace ":" to ","
                        content = StringUtils.replace(content, ":", ",");
                        // String[] to Map
                        Map<String, String> parameters = CollectionUtils
                                .toStringMap(commaDelimitedListToStringArray(content));
                        setValue(parameters);
                    }
                });

        // ignore "registries" field and then use RegistryConfig beans
        dataBinder.setDisallowedFields("registries");
        dataBinder.bind(new MutablePropertyValues(dubboTranslatedAttributes));
    }

    @PreDestroy
    public void destroy() {
        destroyReferenceBeans();
        cache.clear();
    }

    public void destroy(String serviceName) {
        Set<String> removeGroups = new HashSet<>(cache.keySet());
        for (String key : removeGroups) {
            if (key.contains(serviceName)) {
                ReferenceBean<GenericService> referenceBean = cache.remove(key);
                referenceBean.destroy();
            }
        }
    }

    private void destroyReferenceBeans() {
        Collection<ReferenceBean<GenericService>> referenceBeans = cache.values();
        if (logger.isInfoEnabled()) {
            logger.info("The Dubbo GenericService ReferenceBeans are destroying...");
        }
        for (ReferenceBean referenceBean : referenceBeans) {
            referenceBean.destroy(); // destroy ReferenceBean
            if (logger.isInfoEnabled()) {
                logger.info("Destroyed the ReferenceBean  : {} ", referenceBean);
            }
        }
    }

}
