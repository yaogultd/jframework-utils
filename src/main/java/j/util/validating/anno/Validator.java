package j.util.validating.anno;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validator {
    /**
     * 非空
     * @return
     */
    boolean notNull() default false;

    /**
     * 不允许null和”“字符串
     * @return
     */
    boolean notBlank() default false;

    /**
     * 数值类型不允许0值
     * @return
     */
    boolean notZero() default false;

    /**
     * 是否大小写无关
     * @return
     */
    boolean ignoreCase() default false;

    /**
     * 字符串-最少字符数
     * @return
     */
    int minChars() default -1;

    /**
     * 字符串-最多字符数
     * @return
     */
    int maxChars() default -1;

    /**
     * 字符串-最少字节数
     * @return
     */
    int minBytes() default -1;

    /**
     * 字符串-最多字节数
     * @return
     */
    int maxBytes() default -1;

    /**
     * 正则
     * @return
     */
    String regex() default "";

    /**
     * 错误码
     * @return
     */
    String errCode() default "";

    /**
     * 错误信息
     * @return
     */
    String errMessage() default "";

    /**
     * 可选值（多个用分页符\f分隔）
     * @return
     */
    String candidateValues() default "";
}
