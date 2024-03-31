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

        TagDTO program = new TagDTO();
        program.setCategoryName("计算机专业");
        program.setTags(Arrays.asList("操作系统","计算机组成原理","计算机网络","数据结构"));
        tagDTOS.add(program);

        TagDTO framwork = new TagDTO();
        framwork.setCategoryName("金融专业");
        framwork.setTags(Arrays.asList("会计","经济学","统计学","人类社会学"));
        tagDTOS.add(framwork);

        TagDTO framwork1 = new TagDTO();
        framwork1.setCategoryName("土木工程");
        framwork1.setTags(Arrays.asList("结构物理","高等数学","混凝土配置学","八段锦"));
        tagDTOS.add(framwork1);

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
