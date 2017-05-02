package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.*;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by tom.zhu on 16-8-26.
 */
public class CustomWordModuleGetDescImage extends CustomWordModule {

    public final static String moduleName = "GetDescImage";
//	private static Font dynamicFont;

    public CustomWordModuleGetDescImage() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {

        //user param
        CustomModuleUserParamGetDescImage customModuleUserParamGetDescImage = ((CustomWordValueGetDescImage) customWord.getValue()).getUserParam();

        // 设置一些默认推荐值
        String field = null;    // 数据来源字段名称
        int width = 790;        // 固定宽度
        int startX = 30;        // 起始位置x
        int startY = 30;        // 起始位置y
        int sectionSize = 5;    // 行间距
        float fontSize = 18f;      // 文字大小
        int oneLineBit = 70;    // 一行的英文单词数

        {
            // 数据来源字段名称
            RuleExpression ruleExpression = customModuleUserParamGetDescImage.getField();
            if (ruleExpression != null) {
                field = expressionParser.parse(ruleExpression, shopBean, user, extParameter);
            }
        }
        {
            // 固定宽度
            RuleExpression ruleExpression = customModuleUserParamGetDescImage.getWidth();
            if (ruleExpression != null) {
                String tmp = expressionParser.parse(ruleExpression, shopBean, user, extParameter);
                if (!StringUtils.isEmpty(tmp)) {
                    width = Integer.parseInt(tmp);
                }
            }
        }
        {
            // 起始位置x
            RuleExpression ruleExpression = customModuleUserParamGetDescImage.getStartX();
            if (ruleExpression != null) {
                String tmp = expressionParser.parse(ruleExpression, shopBean, user, extParameter);
                if (!StringUtils.isEmpty(tmp)) {
                    startX = Integer.parseInt(tmp);
                }
            }
        }
        {
            // 起始位置y
            RuleExpression ruleExpression = customModuleUserParamGetDescImage.getStartY();
            if (ruleExpression != null) {
                String tmp = expressionParser.parse(ruleExpression, shopBean, user, extParameter);
                if (!StringUtils.isEmpty(tmp)) {
                    startY = Integer.parseInt(tmp);
                }
            }
        }
        {
            // 行间距
            RuleExpression ruleExpression = customModuleUserParamGetDescImage.getSectionSize();
            if (ruleExpression != null) {
                String tmp = expressionParser.parse(ruleExpression, shopBean, user, extParameter);
                if (!StringUtils.isEmpty(tmp)) {
                    sectionSize = Integer.parseInt(tmp);
                }
            }
        }
        {
            // 文字大小
            RuleExpression ruleExpression = customModuleUserParamGetDescImage.getFontSize();
            if (ruleExpression != null) {
                String tmp = expressionParser.parse(ruleExpression, shopBean, user, extParameter);
                if (!StringUtils.isEmpty(tmp)) {
                    fontSize = Integer.parseInt(tmp);
                }
            }
        }
        {
            // 一行的英文单词数
            RuleExpression ruleExpression = customModuleUserParamGetDescImage.getOneLineBit();
            if (ruleExpression != null) {
                String tmp = expressionParser.parse(ruleExpression, shopBean, user, extParameter);
                if (!StringUtils.isEmpty(tmp)) {
                    oneLineBit = Integer.parseInt(tmp);
                }
            }
        }

        // 获取需要设定的文本
        //   文字来源是指定字段名 (默认从platforms.Pxx.fields下面获取, 如果没有, 则从common.fields下面获取)
        //   文字如果为空, 那么就不做任何处理跳出
        //   文字如果希望有回车的话, 请用\n
        String txtDesc = "";

        if (StringUtils.isEmpty(field)) {
            return "";
        }
        txtDesc = sxData.getMainProduct().getPlatform(sxData.getCartId()).getFields().getAttribute(field);
        if (StringUtils.isEmpty(txtDesc)) {
            txtDesc = sxData.getMainProduct().getCommon().getFields().getAttribute(field);
        }
        if (StringUtils.isEmpty(txtDesc)) {
            return "";
        }

        // 万一有html的话, 直接换掉
        txtDesc = txtDesc.replaceAll("<br />", "\n");
        txtDesc = txtDesc.replaceAll("<br>", "\n");
        txtDesc = doClearHtml(txtDesc);

        // 20170419 自定义图片处理逻辑优化 charis STA
        // 制作图片
        byte[] img = doCreateImage(txtDesc, width, startX, startY, sectionSize, fontSize, oneLineBit);

        String orgUrl = "customGetDescImage://" + MD5.getMD5(txtDesc);

        // 到数据库里找一下， 如果有了的话， 直接用数据库里的 platform_image_url
        String url = sxProductService.getPlatformUrl4GetDescImage(orgUrl, sxData, shopBean, user, img);
        // 20170419 自定义图片处理逻辑优化 charis END
        return url;
    }

    public String doClearHtml(String value) {
        String prefix = "< *";
        String suffix = " *>";
        java.util.List<String> lstHtml = new ArrayList<>();
        lstHtml.add(prefix + "br" + suffix);
        lstHtml.add("< *br */>");
        lstHtml.add(prefix + "p" + suffix);
        lstHtml.add(prefix + "/p" + suffix);
        lstHtml.add(prefix + "/ *p" + suffix);
        lstHtml.add(prefix + "ul" + suffix);
        lstHtml.add(prefix + "/ul" + suffix);
        lstHtml.add(prefix + "/ *ul" + suffix);
        lstHtml.add(prefix + "li" + suffix);
        lstHtml.add(prefix + "/li" + suffix);
        lstHtml.add(prefix + "/ *li" + suffix);
        lstHtml.add(prefix + "div" + suffix);
        lstHtml.add(prefix + "/div" + suffix);
        lstHtml.add(prefix + "/ *div" + suffix);
        lstHtml.add("&nbsp;");

        for (String html : lstHtml) {
            value = value.replaceAll(html, " ");
        }

        return value;
    }

    private byte[] doCreateImage(String txtDesc, int width, int startX, int startY, int sectionSize, float fontSize, int oneLineBit) {

        int height;
        String[] split = getChangedString(txtDesc, oneLineBit).split("\n");
        height = startY + ((int)fontSize + sectionSize) * split.length;

        BufferedImage tag = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = tag.createGraphics();
        try {
            Field field = Color.class.getField("white");
            Color color = (Color)field.get(null);
            g2d.setPaint(color);
            g2d.fillRect(0,0,
                    width,
                    height
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // 画字
        for (int i = 0; i < split.length; i++) {
            doDrawText(g2d, split[i], i, fontSize, startX, startY, sectionSize);
        }

        // 图片输出
        try {
            // 写图片
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(tag, "jpg", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return null;
    }

//    private void getFont(){
//		URL fontFangsong = this.getClass().getResource("/config/job/cms/font/Fangsong.ttf");
//
//		File file = new File(fontFangsong.getPath());
//		try {
//			FileInputStream aixing = new FileInputStream(file);
//			dynamicFont = Font.createFont(Font.TRUETYPE_FONT, aixing);
//			aixing.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (FontFormatException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

    private void doDrawText(Graphics2D g2d, String text, int nowLine, float fontSize, int startX, int startY, int sectionSize) {
        {
//			Font dynamicFontPt = new Font("FangSong", Font.BOLD, (int)fontSize); // 仿宋
			Font dynamicFontPt = new Font("WenQuanYi Zen Hei Mono", Font.PLAIN, (int)fontSize); // 文泉驿等宽正黑
//            Font dynamicFontPt = new Font("Noto Sans CJK", Font.PLAIN, (int)fontSize); // 思源黑体
//            Font dynamicFontPt = new Font("Source Han Sans CN", Font.PLAIN, (int)fontSize); // 思源黑体(梁兄说暂时不支持, 除非升级OS版本)

//			if (dynamicFont == null) {
//				getFont();
//			}
//
//			Font dynamicFontPt = dynamicFont.deriveFont(Font.BOLD, fontSize);

			// 设置位置
			if (nowLine == 0) {
				g2d.translate(startX, startY);
			} else {
				g2d.translate(0, fontSize + sectionSize);
			}

			// 消除锯齿
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// 设置颜色
			try {
				Field field = Color.class.getField("black");
				Color color = (Color)field.get(null);
				g2d.setColor(color);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}

			// 写字
			g2d.setFont(dynamicFontPt);
			GlyphVector v = dynamicFontPt.createGlyphVector(g2d.getFontMetrics().getFontRenderContext(), text);
			Shape shape = v.getOutline();
			g2d.fill(shape);

//            g2d.drawString(text, 10, dynamicFontPt.getSize() + 10);



        }

    }

    /**
     * 处理输入的字符串，将字符串分割成以byteLength为宽度的多行字符串。
     * 根据需要，英文字符的空格长度为0.5，汉字的长度为2（GBK编码下，UTF-8下为3），数字英文字母宽度为1.05。
     * @param inputString 输入字符串
     * @param byteLength 以byteLength的长度进行分割（一行显示多宽）
     * @return 处理过的字符串
     */
    public static String getChangedString(String inputString, int byteLength) {
        char[] chars = inputString.toCharArray();
        char[] workChars = new char[chars.length * 2];

        // i为工作数组的角标，length为工作过程中长度,stringLength为字符实际长度,j为输入字符角标
        int i = 0, stringLength = 0;
        float  length = 0;
        for (int j = 0; j < chars.length; i++, j++) {

            // 如果源字符串中有换行符，此处要将工作过程中计算的长度清零
            if (chars[j] == '\n') {
                length = 0;
            }
            try {
                workChars[i] = chars[j];
                //对汉字字符进行处理
                if (Character.toString(chars[j]).getBytes("GBK").length == 2 /*&& chars[j] != '”' && chars[j] != '“'*/) {
                    length++;
                    if (length >= byteLength) {

                        i++;
                        stringLength++;
                        workChars[i] = '\n';

                        length = 0;
                    }
                } else if (Character.toString(chars[j]).getBytes("GBK").length == 1) {
                    //对空格何应为字符和数字进行处理。
                    if (chars[j] == ' ' ) {
                        length -= 0.5;
                    }else {
                        length += 0.05;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            stringLength++;
            length++;
            //长度超过给定的长度，插入\n
            if (length >= byteLength) {
                i++;
                stringLength++;
                workChars[i] = '\n';
                length = 0;
            }
        }
        String outputString = new String(workChars).substring(0, stringLength)/* .trim() */;
        return outputString;
    }

}
