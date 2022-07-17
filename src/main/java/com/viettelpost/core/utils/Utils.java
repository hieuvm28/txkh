package com.viettelpost.core.utils;

import com.viettelpost.core.services.domains.PhoneInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    static List<PhoneInfo> listPhone = Arrays.asList(
            new PhoneInfo("0162", "032"),
            new PhoneInfo("0163", "033"),
            new PhoneInfo("0164", "034"),
            new PhoneInfo("0165", "035"),
            new PhoneInfo("0166", "036"),
            new PhoneInfo("0167", "037"),
            new PhoneInfo("0168", "038"),
            new PhoneInfo("0169", "039"),

            new PhoneInfo("0120", "070"),
            new PhoneInfo("0121", "079"),
            new PhoneInfo("0122", "077"),
            new PhoneInfo("0126", "076"),
            new PhoneInfo("0128", "078"),

            new PhoneInfo("0123", "083"),
            new PhoneInfo("0124", "084"),
            new PhoneInfo("0125", "085"),
            new PhoneInfo("0127", "081"),
            new PhoneInfo("0129", "082"),

            new PhoneInfo("0186", "056"),
            new PhoneInfo("0188", "058"),

            new PhoneInfo("0199", "059")
    );

    public static java.sql.Date StringToSqlDate(String d) throws ParseException {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date parsed = format.parse(d);
            java.sql.Date sql = new java.sql.Date(parsed.getTime());
            return sql;
        } catch (Exception ex) {

        }
        return null;
    }

    public static Date resetHourOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date resetHourOfDateEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static String objectToString(Object object, Logger logger) {
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields != null && fields.length != 0) {
            try {
                String result = object.getClass().getName() + ":{";
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    String key = field.getName();
                    char first = Character.toUpperCase(key.charAt(0));
                    key = first + key.substring(1);
                    String functionName = "get" + key;
                    Class noparams[] = {};
                    try {
                        Method getMethod = object.getClass().getDeclaredMethod(functionName, noparams);
                        Object value = getMethod.invoke(object);
                        if (object instanceof List) {
                            List t = (List) object;
                            result += key + ": List - " + (value == null ? "--" : t.size()) + "elm;";
                        } else {
                            result += key + ":" + (value == null ? "--" : value.toString()) + ";";
                        }
                    } catch (NoSuchMethodException e) {
                        functionName = "is" + key;
                        try {
                            Method getMethod = object.getClass().getDeclaredMethod(functionName, noparams);
                            Object value = getMethod.invoke(object);
                            result += key + ":" + value == null ? "" : value.toString();
                        } catch (NoSuchMethodException e1) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
                if (result.length() > 1) {
                    result = result.substring(0, result.length() - 2);
                }
                result += "}";
                return result;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return "--";
    }

    public static boolean isValidEmail(String emailStr) {
        if (!isNullOrEmpty(emailStr)) {
            Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailStr);
            return matcher.find();
        }
        return false;
    }

    public static boolean isNullOrEmpty(Object input) {
        if (input instanceof String) {
            return input == null || ((String) input).trim().isEmpty();
        }

        if (input instanceof List) {
            return input == null || ((List) input).isEmpty();
        }
        return input == null;
    }

    public static boolean isOK(Object input) {
        if (input instanceof String) {
            return input != null && !((String) input).trim().isEmpty();
        }

        if (input instanceof List) {
            return input != null && !((List) input).isEmpty();
        }
        return input != null;
    }

    public static boolean isNumber(String input) {
        try {
            Long.valueOf(input);
            return true;
        } catch (Exception e) {
            //ignored
        }
        return false;
    }

    public static String convertNoUnicode(String str) {
        try {
            str = str.trim();
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "").replaceAll("đ", "d");
        } catch (Exception e) {
            //ignored
        }
        return "";
    }

    public static String convertNoUnicodeNormal(String str) {
        try {
            str = str.trim();
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll("\u0111", "d").replaceAll("\u0110", "d");
        } catch (Exception e) {
            //ignored
        }
        return "";
    }

    public static String convertNoUnicodeNoSpace(String str) {
        try {
            str = str.trim();
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d")
                    .replaceAll("\u0111", "d").replaceAll("\u0110", "d")
                    .replaceAll("\r\n", "")
                    .replaceAll("\n", "")
                    ;
        } catch (Exception e) {
            //ignored
        }
        return "";
    }

    public static boolean compareDate(Date from, Date toDate, long time) {
        return from.getTime() - toDate.getTime() > time;
    }

    public static Date setDate(Long value) {
        if (value != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, value.intValue());
            return calendar.getTime();
        }
        return new Date();
    }

    public static Date plusDate(Date date, int value) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, value);
            return c.getTime();
        } catch (Exception e) {

        }
        return null;
    }

    public static Date dateToDateFormat(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.parse(format.format(date));
    }

    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static String dateToStringByFormat(String format, Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String defaultFormatNumber(Long value) {
        try {
            DecimalFormat numFormat = new DecimalFormat("#,###,###");
            return numFormat.format(value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    public static Long weightConvert(String serviceCode, Long length, Long width, Long height) {
        Long weight = 0l;
        if (length == null) {
            length = 0l;
        }
        if (width == null) {
            width = 0l;
        }
        if (height == null) {
            height = 0l;
        }
        if (height.compareTo(0l) < 0 || height.compareTo(0l) < 0 || height.compareTo(0l) < 0) {
            if (serviceCode.equals("VBE") || serviceCode.equals("VBE")) {
                weight = -1l;
            } else {
                weight = 0l;
            }
        } else {
            switch (serviceCode) {
                case "SCOD":
                    if ((length > 80) && (width > 80) && (height > 80)) {
                        weight = (length * width * height) * 1000 / 6000;
                    }
                    break;
                case "PTN":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "PHT":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "PHS":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "VCN":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "VHT":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "VTK":
                    weight = (length * width * height) * 1000 / 4000;
                    break;
                case "V60":
                    weight = (length * width * height) * 1000 / 4500;
                    break;
                case "VBE":
                    weight = ortherWeightConvert(length, width, height);
                    if (weight.compareTo(0l) == 0) {
                        weight = -1l;
                    }
                    break;
                case "VBS":
                    weight = ortherWeightConvert(length, width, height);
                    if (weight.compareTo(0l) == 0) {
                        weight = -1l;
                    }
                    break;
                default: {
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                }
            }
        }
        return weight.compareTo(0l) < 0 ? -1l : weight;
    }

    static Long ortherWeightConvert(Long length, Long width, Long height) {
        Long result = 0l;
        Long total = length + width + height;
        if (length.compareTo(0l) < 0 || width.compareTo(0l) < 0 || height.compareTo(0l) < 0) {
            return 0l;
        }
        if (length.compareTo(0l) == 0 || width.compareTo(0l) == 0 || height.compareTo(0l) == 0) {
            if (total.compareTo(40l) <= 0) {
                result = 100l;
            } else if (total.compareTo(60l) <= 0) {
                result = 500l;
            }
        } else {
            if (total.compareTo(15l) <= 0) {
                result = 100l;
            } else if (total.compareTo(30l) <= 0) {
                result = 500l;
            } else if (total.compareTo(40l) <= 0) {
                result = 1000l;
            } else {
                if (total.compareTo(60l) <= 0) {
                    result = 2000l;
                } else if (total.compareTo(80l) <= 0) {
                    result = 4000l;
                } else if (total.compareTo(100l) <= 0) {
                    result = 7000l;
                } else if (total.compareTo(120l) <= 0) {
                    result = 15000l;
                } else if (total.compareTo(140l) <= 0) {
                    result = 20000l;
                } else if (total.compareTo(160l) <= 0) {
                    result = 25000l;
                }
            }
        }
        return result;
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean safeEqual(Object s1, Object s2) {
        if (s1 != null && s2 != null) {
            if (s1 instanceof Long && s2 instanceof Long) {
                return ((Long) s1).compareTo((Long) s2) == 0;
            }
            if (s1 instanceof String && s2 instanceof String) {
                return ((String) s1).toUpperCase().equals(((String) s2).toUpperCase());
            }
        }
        return false;
    }

    public static String md52(String input) {
        if (!isNullOrEmpty(input)) {
            return md5(md5(input));
        }
        return null;
    }

    public static byte[] exportExcelWithResultSet(String templatePath, Map params, ResultSet rs) {
        try {
            /*JRResultSetDataSource jrResultSetDataSource;
            jrResultSetDataSource = new JRResultSetDataSource(rs);
            JasperPrint jasperPrint = JasperFillManager.fillReport(templatePath, params, jrResultSetDataSource);*/

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            /*JRXlsxExporter exporterXLSx = new JRXlsxExporter();
            exporterXLSx.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporterXLSx.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporterXLSx.exportReport();*/
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            writeLog(logger, e);
        }
        return new byte[]{};
    }

    public static void writeLog(Logger logger, Object ex) {
        if (ex instanceof Exception) {
            Exception e = (Exception) ex;
            logger.error(e.getMessage(), e);
        } else if (ex instanceof String) {
            logger.error(ex.toString());
        } else {
            logger.error(Utils.objectToString(ex, logger));
        }
    }

    public static String parseDateTimeToString(Date date) {
        SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd");
        String dateString = DATE_TIME_FORMAT.format(date);
        return dateString;
    }

    public static String epochToDateString(Long epoch) {
        if (epoch == null)
            return "N/A";
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(epoch));
    }

    public static Long dateToEpoch(Date date) {
        if (date == null)
            return null;
        return date.getTime();
    }

    public static boolean isBrandNameValid(String brandName) {
        try {
            //tu 1 den 101 ky tu chu hoa, thuong va so
            String PASS_PATTERN = "[A-Za-z0-9-_.]{1,11}";
            Pattern pattern = Pattern.compile(PASS_PATTERN);

            Matcher matcher = pattern.matcher(brandName);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPhieuGui(String phieuGui) {
        try {
            //tu 1 den 50 ky tu chu hoa, thuong va so
            String PASS_PATTERN = "[A-Za-z0-9-_.]{1,50}";
            Pattern pattern = Pattern.compile(PASS_PATTERN);

            Matcher matcher = pattern.matcher(phieuGui);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getIsdn(String phone) {
        try {
            String nphone = phone.replaceAll("\\W", "").replaceAll("[a-zA-Z]", "");
            return "" + Long.valueOf(nphone);
        } catch (Exception e) {
            //
        }
        return phone;
    }

    public static String[] validate84Phone(String phone) {
        phone = phone.trim();
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }
        if (!Utils.isNumber(phone)) {
            return new String[]{};
        }
        if (phone.startsWith("84") && phone.length() > 9) {
            phone = phone.substring(2);
        }
        if (phone.startsWith("000")) {
            phone = phone.substring(3);
        }
        if (phone.startsWith("00")) {
            phone = phone.substring(2);
        }
        if (phone.startsWith("0")) {
            phone = phone.substring(1);
        }
        if (phone.length() > 10) {
            return new String[]{};
        }
        if ((phone.startsWith("3") || phone.startsWith("5") || phone.startsWith("7") || phone.startsWith("8") || phone.startsWith("9")) && phone.length() != 9) {
            return new String[]{};
        }
        if (phone.length() >= 9 && !phone.startsWith("18") && !phone.startsWith("19")) {
            phone = "84" + phone;
        } else {
            return new String[]{phone, phone};
        }
        for (PhoneInfo phoneData : listPhone) {
            if (phoneData.equals(new PhoneInfo(phone.substring(0, 4), null))) {
                return new String[]{phoneData.getNewPhone() + phone.substring(4), phoneData.getOldPhone() + phone.substring(4)};
            }
            if (phoneData.equals(new PhoneInfo(null, phone.substring(0, 3)))) {
                return new String[]{phoneData.getNewPhone() + phone.substring(3), phoneData.getOldPhone() + phone.substring(3)};
            }
        }
        return new String[]{phone, phone};
    }

    public static String getValid84Phone(String phone) {
        if (!Utils.isNumber(phone)) {
            return null;
        }
        String[] arr = validate84Phone(phone);
        String str = null;
        if (arr.length > 0 && !isNullOrEmpty(arr[0])) {
            str = arr[0];
            return str;
        }
        return null;
    }
}
