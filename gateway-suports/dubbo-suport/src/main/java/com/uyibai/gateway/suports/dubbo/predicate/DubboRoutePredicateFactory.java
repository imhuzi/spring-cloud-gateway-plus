package com.uyibai.gateway.suports.dubbo.predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.style.ToStringCreator;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import com.uyibai.gateway.common.constants.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * dubbo 服务路由 + 服务信息解析
 * <p>
 * 参考 PathRoutePredicateFactory 实现方式
 */
@Component
@Slf4j
public class DubboRoutePredicateFactory extends AbstractRoutePredicateFactory<DubboRoutePredicateFactory.Config> {
    private static final String MATCH_OPTIONAL_TRAILING_SEPARATOR_KEY = "matchOptionalTrailingSeparator";
    private PathPatternParser pathPatternParser = new PathPatternParser();

    public DubboRoutePredicateFactory() {
        super(Config.class);
    }

    public DubboRoutePredicateFactory(Class<Config> config) {
        super(config);
    }

    private static void traceMatch(String prefix, Object desired, Object actual, boolean match) {
        if (log.isTraceEnabled()) {
            String message = String.format("%s \"%s\" %s against value \"%s\"", prefix, desired, match ? "matches" : "does not match", actual);
            log.trace(message);
        }

    }

    public void setPathPatternParser(PathPatternParser pathPatternParser) {
        this.pathPatternParser = pathPatternParser;
    }


    public List<String> shortcutFieldOrder() {
        return Arrays.asList("patterns", MATCH_OPTIONAL_TRAILING_SEPARATOR_KEY);
    }

    public ShortcutType shortcutType() {
        return ShortcutType.GATHER_LIST_TAIL_FLAG;
    }


    public Predicate<ServerWebExchange> apply(Config config) {
        final ArrayList<PathPattern> pathPatterns = new ArrayList();
        synchronized (this.pathPatternParser) {
            this.pathPatternParser.setMatchOptionalTrailingSeparator(config.isMatchOptionalTrailingSeparator());
            config.getPatterns().forEach((pattern) -> {
                PathPattern pathPattern = this.pathPatternParser.parse(pattern);
                pathPatterns.add(pathPattern);
            });
        }

        return new GatewayPredicate() {
            public boolean test(ServerWebExchange exchange) {
                PathContainer path = PathContainer.parsePath(exchange.getRequest().getURI().getRawPath());
                Optional<PathPattern> optionalPathPattern = pathPatterns.stream().filter((pattern) -> {
                    return pattern.matches(path);
                }).findFirst();
                if (optionalPathPattern.isPresent()) {
                    PathPattern pathPattern = (PathPattern) optionalPathPattern.get();
                    DubboRoutePredicateFactory.traceMatch("Pattern", pathPattern.getPatternString(), path, true);
                    PathPattern.PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(path);
                    exchange.getAttributes().put(Constants.DUBBO_REST_TAG_ROUTE, Constants.DUBBO_REST_TAG_ROUTE);
                    ServerWebExchangeUtils.putUriTemplateVariables(exchange, pathMatchInfo.getUriVariables());
                    return true;
                } else {
                    DubboRoutePredicateFactory.traceMatch("Pattern", config.getPatterns(), path, false);
                    return false;
                }
            }

            public String toString() {
                return String.format("Paths: %s, match trailing slash: %b", config.getPatterns(), config.isMatchOptionalTrailingSeparator());
            }
        };
    }


    public static class Config {
        //Put the configuration properties for your filter here
        private List<String> patterns = new ArrayList();
        private boolean matchOptionalTrailingSeparator = true;

        public Config() {
        }


        public List<String> getPatterns() {
            return this.patterns;
        }

        public Config setPatterns(List<String> patterns) {
            this.patterns = patterns;
            return this;
        }

        public boolean isMatchOptionalTrailingSeparator() {
            return this.matchOptionalTrailingSeparator;
        }

        public Config setMatchOptionalTrailingSeparator(boolean matchOptionalTrailingSeparator) {
            this.matchOptionalTrailingSeparator = matchOptionalTrailingSeparator;
            return this;
        }

        public String toString() {
            return (new ToStringCreator(this)).append("patterns", this.patterns).append("matchOptionalTrailingSeparator", this.matchOptionalTrailingSeparator).toString();
        }

    }
}
