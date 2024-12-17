package com.lifetime.common.util;




import com.lifetime.common.enums.LevelEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PWUtil {

    /**
     * NUM 数字
     * SMALL_LETTER 小写字母
     * CAPITAL_LETTER 大写字母
     * OTHER_CHAR 特殊字符
     */
    private static final int NUM = 1;
    private static final int SMALL_LETTER = 2;
    private static final int CAPITAL_LETTER = 3;
    private static final int OTHER_CHAR = 4;
    /**
     * 简单的密码字典
     */
    private final static String[] DICTIONARY = {"password", "abc123", "iloveyou", "adobe123", "123123", "sunshine",
            "1314520", "a1b2c3", "123qwe", "aaa111", "qweasd", "admin", "passwd"};

    private static Integer PW_LENTGTH =8;
    private static Integer CODE_LENTGTH=4;

    public static String createPassword() {
        PW_LENTGTH = PW_LENTGTH > 8 ? PW_LENTGTH : 8;

        String[] password = new String[PW_LENTGTH];
        Random rand = new Random();
        int lowercase = rand.nextInt(26) + 65;
        int upercase = rand.nextInt(26) + 97;
        int figure = rand.nextInt(10) + 48;
        int special = rand.nextInt(15) + 33;
        //去掉‘"
        if (special == 34 || special == 39) {
            special = 35;
        }
        password[0]=((char) lowercase)+"";
        password[1]=((char) upercase)+"";
        password[2]=((char) figure)+"";
        password[3]=((char) special)+"";
        for (int i = 4; i < PW_LENTGTH; i++) {
            int choice = rand.nextInt(4);
            lowercase = rand.nextInt(26) + 65;
            upercase = rand.nextInt(26) + 97;
            figure = rand.nextInt(10) + 48;
            special = rand.nextInt(15) + 33;
            if (special == 34 || special == 39) {
                special = 35;
            }
            switch (choice) {
                case 0:
                    password[i] = ((char) lowercase)+"";
                    break;
                case 1:
                    password[i] = ((char) upercase)+"";
                    break;
                case 2:
                    password[i] = ((char) figure)+"";
                    break;
                case 3:
                    password[i] = ((char) special)+"";
                    break;
                default:
                    break;
            }
        }
        List ret =Arrays.asList(password);
        Collections.shuffle(ret);

        return String.join("",ret);
    }


    /**
     *检查字符类型，包括num、大写字母、小写字母和其他字符。
     *
     * @param c
     * @return
     */
    private static int checkCharacterType(char c) {
        if (c >= 48 && c <= 57) {
            return NUM;
        }
        if (c >= 65 && c <= 90) {
            return CAPITAL_LETTER;
        }
        if (c >= 97 && c <= 122) {
            return SMALL_LETTER;
        }
        return OTHER_CHAR;
    }
    /**
     * 按不同类型计算密码的数量
     *
     * @param passwd
     * @param type
     * @return
     */
    private static int countLetter(String passwd, int type) {
        int count = 0;
        if (null != passwd && passwd.length() > 0) {
            for (char c : passwd.toCharArray()) {
                if (checkCharacterType(c) == type) {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * 检查密码的强度
     *
     * @param passwd
     * @return strength level
     */
    public static int checkPasswordStrength(String passwd) {
        if (StringUtils.equalsNull(passwd)) {
            throw new IllegalArgumentException("password is empty");
        }
        int len = passwd.length();
        int level = 0;
        // 增加点
        //判断密码是否含有数字有level++
        if (countLetter(passwd, NUM) > 0) {
            level++;
        }
        //判断密码是否含有小写字母有level++
        if (countLetter(passwd, SMALL_LETTER) > 0) {
            level++;
        }
        //判断密码是否还有大写字母有level++
        if (len > 4 && countLetter(passwd, CAPITAL_LETTER) > 0) {
            level++;
        }
        //判断密码是否还有特殊字符有level++
        if (len > 6 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }
        //密码长度大于4并且2种类型组合......（不一一概述）
        if (len > 4 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                || countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }
        //密码长度大于6并且3中类型组合......（不一一概述）
        if (len > 6 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0
                && countLetter(passwd, CAPITAL_LETTER) > 0 || countLetter(passwd, NUM) > 0
                && countLetter(passwd, SMALL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0
                || countLetter(passwd, NUM) > 0 && countLetter(passwd, CAPITAL_LETTER) > 0
                && countLetter(passwd, OTHER_CHAR) > 0 || countLetter(passwd, SMALL_LETTER) > 0
                && countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }
        //密码长度大于8并且4种类型组合......（不一一概述）
        if (len > 8 && countLetter(passwd, NUM) > 0 && countLetter(passwd, SMALL_LETTER) > 0
                && countLetter(passwd, CAPITAL_LETTER) > 0 && countLetter(passwd, OTHER_CHAR) > 0) {
            level++;
        }
        //密码长度大于6并且2种类型组合每种类型长度大于等于3或者2......（不一一概述）
        if (len > 6 && countLetter(passwd, NUM) >= 3 && countLetter(passwd, SMALL_LETTER) >= 3
                || countLetter(passwd, NUM) >= 3 && countLetter(passwd, CAPITAL_LETTER) >= 3
                || countLetter(passwd, NUM) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, SMALL_LETTER) >= 3 && countLetter(passwd, CAPITAL_LETTER) >= 3
                || countLetter(passwd, SMALL_LETTER) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, CAPITAL_LETTER) >= 3 && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }
        //密码长度大于8并且3种类型组合每种类型长度大于等于3或者2......（不一一概述）
        if (len > 8 && countLetter(passwd, NUM) >= 2 && countLetter(passwd, SMALL_LETTER) >= 2
                && countLetter(passwd, CAPITAL_LETTER) >= 2 || countLetter(passwd, NUM) >= 2
                && countLetter(passwd, SMALL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2
                || countLetter(passwd, NUM) >= 2 && countLetter(passwd, CAPITAL_LETTER) >= 2
                && countLetter(passwd, OTHER_CHAR) >= 2 || countLetter(passwd, SMALL_LETTER) >= 2
                && countLetter(passwd, CAPITAL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }
        //密码长度大于10并且4种类型组合每种类型长度大于等于2......（不一一概述）
        if (len > 10 && countLetter(passwd, NUM) >= 2 && countLetter(passwd, SMALL_LETTER) >= 2
                && countLetter(passwd, CAPITAL_LETTER) >= 2 && countLetter(passwd, OTHER_CHAR) >= 2) {
            level++;
        }
        //特殊字符>=3 level++;
        if (countLetter(passwd, OTHER_CHAR) >= 3) {
            level++;
        }
        //特殊字符>=6 level++;
        if (countLetter(passwd, OTHER_CHAR) >= 6) {
            level++;
        }
        //长度>12 >16 level++
        if (len > 12) {
            level++;
            if (len >= 16) {
                level++;
            }
        }
        // 减少点
        if ("abcdefghijklmnopqrstuvwxyz".indexOf(passwd) > 0 || "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(passwd) > 0) {
            level--;
        }
        if ("qwertyuiop".indexOf(passwd) > 0 || "asdfghjkl".indexOf(passwd) > 0 || "zxcvbnm".indexOf(passwd) > 0) {
            level--;
        }
        if (StringUtils.isNumeric(passwd) && ("01234567890".indexOf(passwd) > 0 || "09876543210".indexOf(passwd) > 0)) {
            level--;
        }
        if (countLetter(passwd, NUM) == len || countLetter(passwd, SMALL_LETTER) == len
                || countLetter(passwd, CAPITAL_LETTER) == len) {
            level--;
        }
        if (len % 2 == 0) { // aaabbb
            String part1 = passwd.substring(0, len / 2);
            String part2 = passwd.substring(len / 2);
            if (part1.equals(part2)) {
                level--;
            }
            if (StringUtils.isCharEqual(part1) && StringUtils.isCharEqual(part2)) {
                level--;
            }
        }
        if (len % 3 == 0) { // ababab
            String part1 = passwd.substring(0, len / 3);
            String part2 = passwd.substring(len / 3, len / 3 * 2);
            String part3 = passwd.substring(len / 3 * 2);
            if (part1.equals(part2) && part2.equals(part3)) {
                level--;
            }
        }
        if (StringUtils.isNumeric(passwd) && len >= 6) { // 19881010 or 881010
            int year = 0;
            if (len == 8 || len == 6) {
                year = Integer.parseInt(passwd.substring(0, len - 4));
            }
            int size = StringUtils.sizeOfInt(year);
            int month = Integer.parseInt(passwd.substring(size, size + 2));
            int day = Integer.parseInt(passwd.substring(size + 2, len));
            if (year >= 1950 && year < 2050 && month >= 1 && month <= 12 && day >= 1 && day <= 31) {
                level--;
            }
        }
        if (null != DICTIONARY && DICTIONARY.length > 0) {// dictionary
            for (int i = 0; i < DICTIONARY.length; i++) {
                if (passwd.equals(DICTIONARY[i]) || DICTIONARY[i].indexOf(passwd) >= 0) {
                    level--;
                    break;
                }
            }
        }
        if (len <= 6) {
            level--;
            if (len <= 4) {
                level--;
                if (len <= 3) {
                    level = 0;
                }
            }
        }
        if (StringUtils.isCharEqual(passwd)) {
            level = 0;
        }
        if (level < 0) {
            level = 0;
        }
        return level;
    }
    /**
     *获得密码强度等级，包括简单、复杂、强、强、强
     *
     * @param passwd
     * @return
     */
    public static LevelEnum getPasswordLevel(String passwd) {
        int level = checkPasswordStrength(passwd);
        switch (level) {
            case 0:
            case 1:
            case 2:
            case 3:
                return LevelEnum.EASY;
            case 4:
            case 5:
            case 6:
                return LevelEnum.MIDIUM;
            case 7:
            case 8:
            case 9:
                return LevelEnum.STRONG;
            case 10:
            case 11:
            case 12:
                return LevelEnum.VERY_STRONG;
            default:
                return LevelEnum.EXTREMELY_STRONG;
        }
    }



    public static boolean checkPassword(String pas) {
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!-/]).{8,16}$");
        Matcher matcher = pattern.matcher(pas);
        return matcher.matches();
    }

    public static String createCode(){
        Random rand = new Random();
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < CODE_LENTGTH; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();

    }

}
