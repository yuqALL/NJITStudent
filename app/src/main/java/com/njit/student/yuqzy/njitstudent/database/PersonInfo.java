package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/8.
 */

public class PersonInfo extends RealmObject {
    private String personZP;//照片
    private String personXH;//学号
    private String personXM;//姓名
    private String personCYM;//曾用名
    private String personXB;//性别
    private String personMZ;//民族
    private String personZZMM;//政治面貌
    private String personXY;//学院
    private String personX;//系
    private String personXZB;//行政班
    private String personJXBMC;//教学班名称
    private String personZYMC;//专业名称
    private String personZYFX;//专业方向
    private String personPYFX;//培养方向
    private String personRXRQ;//入学日期
    private String personCSRQ;//出生日期
    private String personSFZH;//身份证号

    private String personXSZH;//学生证号
    private String personBYZX;//毕业中学
    private String personSSH;//宿舍号
    private String personLXDH;//联系电话
    private String personSJHM;//手机号码
    private String personDZYX;//电子邮箱
    private String personSJLX;//手机类型
    private String personJG;//籍贯
    private String personLYDQ;//来源地区
    private String personYZBM;//邮政编码
    private String personZKZH;//准考证号
    private String personXXNX;//学习年限
    private String personCSD;//出生地


    private String personJKZK;//健康状况
    private String personXLCC;//学历层次

    private String personLYS;//来源省
    private String personFQXM;//父亲姓名
    private String personFQDW;//父亲单位
    private String personFQDWYX;//父亲单位邮编
    private String personFQDWDHHSJ;//父亲单位电话或手机

    private String personMQXM;//母亲姓名
    private String personMQDW;//母亲单位
    private String personMQDWYB;//母亲单位邮编
    private String personMQDWDHHSJ;//母亲单位电话或手机




    private String personJTDH;//家庭电话
    private String personJTYB;//家庭邮编




    private String personGATM;//港澳台码
    private String personJTDZ;//家庭地址

    private String personBDH;//报到号
    private String personJTSZD;//家庭所在地（/省/县）

    private String personSFGSPYDY;//是否高水平运动员
    private String personBZ;//备注

    private String personYYDJ;//英语等级

    private String personYYCJ;//英语成绩
    private String personXZ;//学制
    private String personLJBYM;//录检表页码

    private String personTC;//特长
    private String personXJZT;//学籍状态
    private String personRTSJ;//入党(团)时间
    private String personDQSZJ;//当前所在级
    private String personHCZDZ;//火车终点站
    private String personZMR;//证明人
    private String personKSH;//考生号
    private String personXXXS;//学习形式
    private String personXMPY;//姓名拼音

    public String getPersonYZBM() {
        return personYZBM;
    }

    public void setPersonYZBM(String personYZBM) {
        this.personYZBM = personYZBM;
    }

    public String getPersonMQDW() {
        return personMQDW;
    }

    public void setPersonMQDW(String personMQDW) {
        this.personMQDW = personMQDW;
    }

    public String getPersonLYS() {
        return personLYS;
    }

    public void setPersonLYS(String personLYS) {
        this.personLYS = personLYS;
    }

    public String getPersonZKZH() {
        return personZKZH;
    }

    public void setPersonZKZH(String personZKZH) {
        this.personZKZH = personZKZH;
    }

    public String getPersonMQDWYB() {
        return personMQDWYB;
    }

    public void setPersonMQDWYB(String personMQDWYB) {
        this.personMQDWYB = personMQDWYB;
    }

    public String getPersonCSD() {
        return personCSD;
    }

    public void setPersonCSD(String personCSD) {
        this.personCSD = personCSD;
    }

    public String getPersonSFZH() {
        return personSFZH;
    }

    public void setPersonSFZH(String personSFZH) {
        this.personSFZH = personSFZH;
    }

    public String getPersonFQDWDHHSJ() {
        return personFQDWDHHSJ;
    }

    public void setPersonFQDWDHHSJ(String personFQDWDHHSJ) {
        this.personFQDWDHHSJ = personFQDWDHHSJ;
    }

    public String getPersonJKZK() {
        return personJKZK;
    }

    public void setPersonJKZK(String personJKZK) {
        this.personJKZK = personJKZK;
    }

    public String getPersonXLCC() {
        return personXLCC;
    }

    public void setPersonXLCC(String personXLCC) {
        this.personXLCC = personXLCC;
    }

    public String getPersonMQDWDHHSJ() {
        return personMQDWDHHSJ;
    }

    public void setPersonMQDWDHHSJ(String personMQDWDHHSJ) {
        this.personMQDWDHHSJ = personMQDWDHHSJ;
    }

    public String getPersonXY() {
        return personXY;
    }

    public void setPersonXY(String personXY) {
        this.personXY = personXY;
    }

    public String getPersonGATM() {
        return personGATM;
    }

    public void setPersonGATM(String personGATM) {
        this.personGATM = personGATM;
    }

    public String getPersonJTDZ() {
        return personJTDZ;
    }

    public void setPersonJTDZ(String personJTDZ) {
        this.personJTDZ = personJTDZ;
    }

    public String getPersonX() {
        return personX;
    }

    public void setPersonX(String personX) {
        this.personX = personX;
    }

    public String getPersonBDH() {
        return personBDH;
    }

    public void setPersonBDH(String personBDH) {
        this.personBDH = personBDH;
    }

    public String getPersonJTSZD() {
        return personJTSZD;
    }

    public void setPersonJTSZD(String personJTSZD) {
        this.personJTSZD = personJTSZD;
    }

    public String getPersonZYMC() {
        return personZYMC;
    }

    public void setPersonZYMC(String personZYMC) {
        this.personZYMC = personZYMC;
    }

    public String getPersonSFGSPYDY() {
        return personSFGSPYDY;
    }

    public void setPersonSFGSPYDY(String personSFGSPYDY) {
        this.personSFGSPYDY = personSFGSPYDY;
    }

    public String getPersonBZ() {
        return personBZ;
    }

    public void setPersonBZ(String personBZ) {
        this.personBZ = personBZ;
    }

    public String getPersonJXBMC() {
        return personJXBMC;
    }

    public void setPersonJXBMC(String personJXBMC) {
        this.personJXBMC = personJXBMC;
    }

    public String getPersonYYDJ() {
        return personYYDJ;
    }

    public void setPersonYYDJ(String personYYDJ) {
        this.personYYDJ = personYYDJ;
    }

    public String getPersonXZB() {
        return personXZB;
    }

    public void setPersonXZB(String personXZB) {
        this.personXZB = personXZB;
    }

    public String getPersonYYCJ() {
        return personYYCJ;
    }

    public void setPersonYYCJ(String personYYCJ) {
        this.personYYCJ = personYYCJ;
    }

    public String getPersonXZ() {
        return personXZ;
    }

    public void setPersonXZ(String personXZ) {
        this.personXZ = personXZ;
    }

    public String getPersonLJBYM() {
        return personLJBYM;
    }

    public void setPersonLJBYM(String personLJBYM) {
        this.personLJBYM = personLJBYM;
    }

    public String getPersonXXNX() {
        return personXXNX;
    }

    public void setPersonXXNX(String personXXNX) {
        this.personXXNX = personXXNX;
    }

    public String getPersonTC() {
        return personTC;
    }

    public void setPersonTC(String personTC) {
        this.personTC = personTC;
    }

    public String getPersonXJZT() {
        return personXJZT;
    }

    public void setPersonXJZT(String personXJZT) {
        this.personXJZT = personXJZT;
    }

    public String getPersonRTSJ() {
        return personRTSJ;
    }

    public void setPersonRTSJ(String personRTSJ) {
        this.personRTSJ = personRTSJ;
    }

    public String getPersonDQSZJ() {
        return personDQSZJ;
    }

    public void setPersonDQSZJ(String personDQSZJ) {
        this.personDQSZJ = personDQSZJ;
    }

    public String getPersonHCZDZ() {
        return personHCZDZ;
    }

    public void setPersonHCZDZ(String personHCZDZ) {
        this.personHCZDZ = personHCZDZ;
    }

    public String getPersonZMR() {
        return personZMR;
    }

    public void setPersonZMR(String personZMR) {
        this.personZMR = personZMR;
    }

    public String getPersonKSH() {
        return personKSH;
    }

    public void setPersonKSH(String personKSH) {
        this.personKSH = personKSH;
    }

    public String getPersonXXXS() {
        return personXXXS;
    }

    public void setPersonXXXS(String personXXXS) {
        this.personXXXS = personXXXS;
    }

    public String getPersonXMPY() {
        return personXMPY;
    }

    public void setPersonXMPY(String personXMPY) {
        this.personXMPY = personXMPY;
    }


    public String getPersonZP() {
        return personZP;
    }

    public void setPersonZP(String personZP) {
        this.personZP = personZP;
    }

    public String getPersonXH() {
        return personXH;
    }

    public void setPersonXH(String personXH) {
        this.personXH = personXH;
    }

    public String getPersonXSZH() {
        return personXSZH;
    }

    public void setPersonXSZH(String personXSZH) {
        this.personXSZH = personXSZH;
    }

    public String getPersonSJLX() {
        return personSJLX;
    }

    public void setPersonSJLX(String personSJLX) {
        this.personSJLX = personSJLX;
    }

    public String getPersonXM() {
        return personXM;
    }

    public void setPersonXM(String personXM) {
        this.personXM = personXM;
    }

    public String getPersonPYFX() {
        return personPYFX;
    }

    public void setPersonPYFX(String personPYFX) {
        this.personPYFX = personPYFX;
    }

    public String getPersonSJHM() {
        return personSJHM;
    }

    public void setPersonSJHM(String personSJHM) {
        this.personSJHM = personSJHM;
    }

    public String getPersonCYM() {
        return personCYM;
    }

    public void setPersonCYM(String personCYM) {
        this.personCYM = personCYM;
    }

    public String getPersonZYFX() {
        return personZYFX;
    }

    public void setPersonZYFX(String personZYFX) {
        this.personZYFX = personZYFX;
    }

    public String getPersonJTYB() {
        return personJTYB;
    }

    public void setPersonJTYB(String personJTYB) {
        this.personJTYB = personJTYB;
    }

    public String getPersonXB() {
        return personXB;
    }

    public void setPersonXB(String personXB) {
        this.personXB = personXB;
    }

    public String getPersonRXRQ() {
        return personRXRQ;
    }

    public void setPersonRXRQ(String personRXRQ) {
        this.personRXRQ = personRXRQ;
    }

    public String getPersonJTDH() {
        return personJTDH;
    }

    public void setPersonJTDH(String personJTDH) {
        this.personJTDH = personJTDH;
    }

    public String getPersonCSRQ() {
        return personCSRQ;
    }

    public void setPersonCSRQ(String personCSRQ) {
        this.personCSRQ = personCSRQ;
    }

    public String getPersonBYZX() {
        return personBYZX;
    }

    public void setPersonBYZX(String personBYZX) {
        this.personBYZX = personBYZX;
    }

    public String getPersonFQXM() {
        return personFQXM;
    }

    public void setPersonFQXM(String personFQXM) {
        this.personFQXM = personFQXM;
    }

    public String getPersonMZ() {
        return personMZ;
    }

    public void setPersonMZ(String personMZ) {
        this.personMZ = personMZ;
    }

    public String getPersonSSH() {
        return personSSH;
    }

    public void setPersonSSH(String personSSH) {
        this.personSSH = personSSH;
    }

    public String getPersonFQDW() {
        return personFQDW;
    }

    public void setPersonFQDW(String personFQDW) {
        this.personFQDW = personFQDW;
    }

    public String getPersonJG() {
        return personJG;
    }

    public void setPersonJG(String personJG) {
        this.personJG = personJG;
    }

    public String getPersonDZYX() {
        return personDZYX;
    }

    public void setPersonDZYX(String personDZYX) {
        this.personDZYX = personDZYX;
    }

    public String getPersonFQDWYX() {
        return personFQDWYX;
    }

    public void setPersonFQDWYX(String personFQDWYX) {
        this.personFQDWYX = personFQDWYX;
    }

    public String getPersonZZMM() {
        return personZZMM;
    }

    public void setPersonZZMM(String personZZMM) {
        this.personZZMM = personZZMM;
    }

    public String getPersonLXDH() {
        return personLXDH;
    }

    public void setPersonLXDH(String personLXDH) {
        this.personLXDH = personLXDH;
    }

    public String getPersonMQXM() {
        return personMQXM;
    }

    public void setPersonMQXM(String personMQXM) {
        this.personMQXM = personMQXM;
    }

    public String getPersonLYDQ() {
        return personLYDQ;
    }

    public void setPersonLYDQ(String personLYDQ) {
        this.personLYDQ = personLYDQ;
    }
}
