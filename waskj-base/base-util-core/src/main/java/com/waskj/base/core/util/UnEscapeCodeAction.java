package com.waskj.base.core.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.beetl.json.*;
import org.beetl.json.util.StringQuoteCheck;

/**
 * 增强StringQuoteCheck（如果遇到双引号和换行符，添加一个自定义动作）
 * 增加对字符串反转义处理功能
 * Created by bojieshen on 2017/6/16 0016.
 */
public class UnEscapeCodeAction extends StringQuoteCheck {

    @Override
    public ActionReturn doit(OutputNodeKey outputNodeKey, Object o, OutputNode outputNode, JsonWriter jsonWriter) {
        if(o instanceof String) {
            return new ActionReturn(this.getString(escapeCode((String)o), jsonWriter), 0);
        } else {
            if(o instanceof DirectOutputValue) {
                DirectOutputValue dov = (DirectOutputValue)o;
                if(dov.getValue() instanceof String) {
                    String newValue = (String)dov.getValue();
                    newValue = this.getString(newValue, jsonWriter);
                    return new ActionReturn(newValue, 0);
                }
            }
            return new ActionReturn(o);
        }
    }

    /**
     * 反转义字符
     * @param input
     * @return
     */
    private String escapeCode(String input){
        return StringEscapeUtils.unescapeHtml4(input);
    }
}
