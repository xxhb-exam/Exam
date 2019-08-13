package com.exam.service.impl;

import java.util.List;

import com.exam.entity.QuestionBank;
import com.exam.util.ReadExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.dao.QuestionBankMapper;
import com.exam.entity.Options;
import com.exam.entity.vo.QuestionBankVo;
import com.exam.service.QuestionBankService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 题库业务类
 */
@Service
public class QuestionBankServiceImpl implements QuestionBankService {

	@Autowired
	private QuestionBankMapper questionBankMapper;

	/**
	 * 查询所有试题
	 */
	@Override
	public PageInfo<QuestionBankVo> findAllQuestionBank(PageInfo<QuestionBankVo> pageInfo) {

		PageHelper.startPage(pageInfo.getPageNum()==0?1:pageInfo.getPageNum(), 40);

		List<QuestionBankVo> AllQuestionBank = questionBankMapper.findAllQuestionBank();

		PageInfo<QuestionBankVo> pageInfoQuestionBank = new PageInfo<QuestionBankVo>(AllQuestionBank);

		return pageInfoQuestionBank;
	}

	//添加题
	@Override
	public boolean addQuestionBank(QuestionBankVo questionBankVo) {
		//设置提状态
		questionBankVo.setState(1);//设置数据有效

		int questionBankState = questionBankMapper.addQuestionBank(questionBankVo);
		if (questionBankVo.getTestsType() == 0) {
			if (questionBankState >= 1) {
				return true;
			}
		}

		for (Options op : questionBankVo.getOptions()) {
			op.setQuestionBankId(questionBankVo.getQuestionBankId());
		}

		int optionsState = questionBankMapper.addOptions(questionBankVo.getOptions());

		if (questionBankState >= 1 && optionsState >= 1) {
			return true;
		}

		return false;
	}

	/**
	 * 删除试题
	 */
	@Override
	public boolean deleteQuestionBank(String questionBankId) {

		int updateQuestionBankState = questionBankMapper.updateQuestionBankState(questionBankId);
		if (updateQuestionBankState >= 1) {
			return true;
		}

		return false;
	}
	/**
	 * 更新题目
	 */
	@Override
	public boolean updateQuestionBank(QuestionBankVo questionBankVo) {
		Integer testsType = questionBankVo.getTestsType();
		int updateQuestionBank = questionBankMapper.updateQuestionBank(questionBankVo);
		if (updateQuestionBank >= 1) {
			questionBankMapper.deleteQuestionBankOptions(questionBankVo.getQuestionBankId().toString());
			if (testsType == 1) {
				for (Options op : questionBankVo.getOptions()) {
					op.setQuestionBankId(questionBankVo.getQuestionBankId());
				}
				questionBankMapper.addOptions(questionBankVo.getOptions());
			}
			return true;
		}

		return false;
	}

	/**
	 *
	 * 从excel中导入题目
	 * 返回值：success or error
	 */
	@Override
	public String readExcelFile(MultipartFile file) {
		String result ="";
		//创建处理EXCEL的类
		ReadExcel readExcel=new ReadExcel();
		//解析excel，获取上传的事件单
		List<QuestionBankVo> questionVoList = readExcel.getExcelInfo(file);
		for (QuestionBankVo questionBankVo:questionVoList) {
			//设置提状态
			questionBankVo.setState(1);//设置数据有效
			int questionBankState = questionBankMapper.addQuestionBank(questionBankVo);
			if (questionBankVo.getTestsType() == 0) {//如果是判断题或填空，则执行
				if (questionBankState >= 1) {
					System.out.println("=========tian jia pan duan cheng gong!");
				}
			}else if(questionBankVo.getTestsType() == 1) {//如果是选择题，则执行
				for (Options op : questionBankVo.getOptions()) {
					op.setQuestionBankId(questionBankVo.getQuestionBankId());
				}
				int optionsState = questionBankMapper.addOptions(questionBankVo.getOptions());
				if (questionBankState >= 1 && optionsState >= 1) {
					System.out.println("=========tian jia xuan ze cheng gong!");
				}
			}
		}
		if(questionVoList != null && !questionVoList.isEmpty()){
			result = "success";
		}else{
			result = "error";
		}
		return result;
	}
}
