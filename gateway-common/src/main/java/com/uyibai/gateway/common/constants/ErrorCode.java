package com.uyibai.gateway.common.constants;

/**
 * 错误码
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date  : 2021/5/26
 */
public class ErrorCode {
  private Integer code;

  private String msg;

  public static ErrorCode SUCCESS = new ErrorCode(200, "执行成功");

  public static ErrorCode EXCUTE_EXCEPTION = new ErrorCode(-1, "执行异常");

  public static ErrorCode SAVE_ERROR = new ErrorCode(100, "新增失败");

  public static ErrorCode SAVE_EXIST_ERROR = new ErrorCode(101, "已经存在，新增失败");

  public static ErrorCode UPDATE_ERROR = new ErrorCode(102, "更新失败");

  public static ErrorCode DELETE_ERROR = new ErrorCode(103, "删除失败");

  public static ErrorCode PARAM_EMPTY = new ErrorCode(104, "参数不能为空");

  public static ErrorCode PARAM_EXCEPTION = new ErrorCode(105, "参数不合法");

  public static ErrorCode NOT_EXIST_ERROR = new ErrorCode(106, "记录信息不存在或已删除");

  private ErrorCode() {
  }

  public ErrorCode(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public ErrorCode setCode(Integer code) {
    this.code = code;
    return this;
  }

  public String getMsg() {
    return msg;
  }

  public ErrorCode setMsg(String msg) {
    this.msg = msg;
    return this;
  }
}
