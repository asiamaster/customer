package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.enums.CustomerEnum;
import com.dili.ss.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 由MyBatis Generator工具自动生成
 * 客户基础数据
企业客户没有性别和民族和certificate_time，但有certificate_rang
 * This file was generated on 2020-01-09 17:36:22.
 * @author yuehongbo
 */
@Getter
@Setter
public class Customer extends BaseDomain {

    private static final long serialVersionUID = -8221228310573544779L;

    private Long id;

    /**
     * 客户编号
     */
    private String code;

    /**
     * 证件号
     */
    private String certificateNumber;

    /**
     * 证件类型
     */
    private String certificateType;

    /**
     * 证件日期##企业时为营业执照日期,如:2011-09-01 至 长期
     */
    private String certificateRange;

    /**
     * 证件是否长期有效 1-是；0-否
     */
    private Integer certificateLongTerm;

    /**
     * 证件地址
     */
    private String certificateAddr;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 出生日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate birthdate;

    /**
     * 性别:男,女
     */
    private Integer gender;

    /**
     * 照片
     */
    private String photo;

    /**
     * 客户等级
     */
    private Integer grade;

    /**
     * 手机号
     */
    private String cellphone;

    /**
     * 联系电话
     */
    private String contactsPhone;

    /**
     * 联系人
     */
    private String contactsName;

    /**
     * 组织类型,个人/企业
     */
    private String organizationType;

    /**
     * 来源系统##外部系统来源标识
     */
    private String sourceSystem;

    /**
     * 来源渠道##租赁业务、系统创建等
     */
    private String sourceChannel;

    /**
     * 客户行业##水果批发/蔬菜批发/超市
     */
    private String profession;

    /**
     * 经营地区##经营地区城市id
     */
    private String operatingArea;

    /**
     * 经营地区经度
     */
    private String operatingLng;

    /**
     * 经营地区纬度
     */
    private String operatingLat;

    /**
     * 其它头衔
     */
    private String otherTitle;

    /**
     * 主营品类
     */

    private String mainCategory;

    /**
     * 注册资金##企业客户属性
     */
    private Long registeredCapital;

    /**
     * 企业员工数
     */
    private String employeeNumber;

    /**
     * 法人证件类型
     */
    private String corporationCertificateType;

    /**
     * 法人证件号
     */
    private String corporationCertificateNumber;

    /**
     * 法人真实姓名
     */
    private String corporationName;

    /**
     * 手机号是否验证
     */
    private Integer isCellphoneValid;

    /**
     * 创建人
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 是否可用
     */
    private Integer isDelete;

    /**
     * 客户状态 0注销，1生效，2禁用，
     */
    private Integer state;

    /**
     * 客户所在市场的创建人
     * 数据来源于客户市场表的创建人
     */
    private Long marketCreatorId;

    /**
     * 客户所在市场的创建时间
     * 数据来源于客户市场表的创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime marketCreateTime;

    /**
     * 客户当前所属的市场
     * 数据来源于客户市场表的市场ID
     */
    private Long marketId;


    /**
     * 客户来源渠道值
     */
    private String sourceChannelValue;

    /**
     * 客户证件号打码加*显示
     */
    private String certificateNumberMask;

    /**
     * 获取客户级别显示
     * @return
     */
    public String getGradeValue(){
        CustomerEnum.Grade instance = CustomerEnum.Grade.getInstance(this.getGrade());
        if (Objects.nonNull(instance)){
            return instance.getValue();
        }
        return "";
    }

    /**
     * 获取客户级别显示
     * @return
     */
    public String getStateValue(){
        CustomerEnum.State instance = CustomerEnum.State.getInstance(this.getState());
        if (Objects.nonNull(instance)){
            return instance.getValue();
        }
        return "";
    }
}