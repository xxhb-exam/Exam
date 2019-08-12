package com.exam.util;

import com.exam.entity.vo.QuestionBankVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomQuestion {

    public static  List<QuestionBankVo> getSubStringByRadom(List<QuestionBankVo> list, int count){
        List backList = null;
        backList = new ArrayList<QuestionBankVo>();
        Random random = new Random();
        int backSum = 0;
        if (list.size() >= count) {
            backSum = count;
        }else {
            backSum = list.size();
        }
        for (int i = 0; i < backSum; i++) {
//			随机数的范围为0-list.size()-1
            int target = random.nextInt(list.size());
            backList.add(list.get(target));
            list.remove(target);
        }
        return backList;
    }

}
