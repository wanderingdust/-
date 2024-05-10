package com.bishe.kaoyan.cache;

import com.bishe.kaoyan.pojo.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();

        TagDTO general = new TagDTO();
        general.setCategoryName("通用");
        general.setTags(Arrays.asList("高等数学","大学英语","思想政治理论","线性代数","概率论与数理统计"));
        tagDTOS.add(general);

        TagDTO computer = new TagDTO();
        computer.setCategoryName("计算机专业");
        computer.setTags(Arrays.asList("操作系统","计算机组成原理","计算机网络","数据结构","离散数学","C语言程序设计"));
        tagDTOS.add(computer);

        TagDTO Chinese = new TagDTO();
        Chinese.setCategoryName("汉语言文学");
        Chinese.setTags(Arrays.asList("中国古典文献学","文艺学","语言学及应用语言学","汉语言文字学","中国古代文学"));
        tagDTOS.add(Chinese);

        TagDTO financial = new TagDTO();
        financial.setCategoryName("金融专业");
        financial.setTags(Arrays.asList("考研数学三","金融学","公司财务","国际金融学"));
        tagDTOS.add(financial);

        TagDTO civilEngineering = new TagDTO();
        civilEngineering.setCategoryName("土木工程");
        civilEngineering.setTags(Arrays.asList("结构力学","工程力学","土木工程信息技术","材料力学"));
        tagDTOS.add(civilEngineering);

        TagDTO mathematics = new TagDTO();
        mathematics.setCategoryName("数学专业");
        mathematics.setTags(Arrays.asList("数学分析","高等代数","解析几何","实变函数与泛函分析","常微分方程"));
        tagDTOS.add(mathematics);

        TagDTO education = new TagDTO();
        education.setCategoryName("教育专业");
        education.setTags(Arrays.asList("教育学原理","教育研究方法","教育统计学","教育经济学"));
        tagDTOS.add(education);

        TagDTO kaoyan = new TagDTO();
        kaoyan.setCategoryName("考研流程");
        kaoyan.setTags(Arrays.asList("心得交流","考研流程"));
        tagDTOS.add(kaoyan);

        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag ->tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }

}
