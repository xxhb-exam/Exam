package com.exam.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exam.util.RandomQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.exam.dao.QuestionBankMapper;
import com.exam.dao.TestPaperMapper;
import com.exam.entity.TestPaper;
import com.exam.entity.TestPaperTestsList;
import com.exam.entity.vo.QuestionBankVo;
import com.exam.entity.vo.TestPaperTestsVo;
import com.exam.service.TestPaperService;
import com.exam.service.TestPaperTestService;

@Controller
@RequestMapping("/admin")
public class TestPaperController {

	@Autowired
	private TestPaperService testPaperService;
	@Autowired
	private QuestionBankMapper questionBankMapper;
	@Autowired
	private TestPaperTestService testPaperTestService;
	@Autowired
	private TestPaperMapper testPaperController;


	//查询数据库中有效试卷
	@RequestMapping(value="/testPaper.html")
	public ModelAndView findTestPaperInfo(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("_admin/test-paper");
		return modelAndView;

	}

	//获取所有试卷内容
	@ResponseBody
	@RequestMapping(value = "/getalltestpaper")
	public List<TestPaper> getAllTestPaper() {
		List<TestPaper> testPaper = testPaperService.findTestPaperInfo();
		return testPaper;
	}

	//更改试卷状态 即为删除
	@RequestMapping(value="/testDelete",method=RequestMethod.GET)
	@ResponseBody
	public String updateTestPaperInfo(String testpaperId){
		int i=testPaperService.updateTestPaperState(Integer.parseInt(testpaperId));
		if(i>=1){
			return "1";
		}else{
			return "0";
		}

	}

	//编辑试卷信息
	@RequestMapping(value="/editTestPaperInfo.html")
	@ResponseBody
	public ModelAndView editTestPaperInfo(HttpServletRequest request,HttpServletResponse response){
		int testpaperId=Integer.parseInt(request.getParameter("testpaperId"));
		TestPaper testPaper=testPaperService.selectByPrimaryKey(testpaperId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("testPaper", testPaper);
		modelAndView.setViewName("_admin/editTestPapperInfo");

		return modelAndView;
	}
	@RequestMapping(value = "/testPaperEdit")
	@ResponseBody
	public String userEdti(TestPaper testPaper)throws Exception{

		int j=testPaperService.updateTestPaperInfo(testPaper);
		if (j >= 1) {
			return "1";
		}
		return "0";

	}
/*
    @RequestMapping(value="/testDelete",method=RequestMethod.GET)
	@ResponseBody
	public String deleteTestPaper(String testpaperId){
    	int i = testPaperService.deleteTestPaper(testpaperId);
    	System.out.println(i);
			if(i>=1){
				return "1";
			}else{
				return "0";
			}

	}
*/

	@ResponseBody
	@RequestMapping(value = "/findalltestpager")
	public List<QuestionBankVo> findAllQuestionBank() {

		List<QuestionBankVo> findAllQuestionBank = questionBankMapper.findAllQuestionBank();

		return findAllQuestionBank;
	}

	@RequestMapping(value = "/addaddtestpaer")
	public boolean addTestPaer() {

		return true;
	}

	@RequestMapping(value="/editItemList.html")
	@ResponseBody
	public ModelAndView editItemList(String testpaperId){
		List<QuestionBankVo> findAllQuestionBank = questionBankMapper.findAllQuestionBank();
		List<TestPaperTestsVo> selectIdTestPaperQuesion=testPaperTestService.findSelectTestPaperQuesion(Integer.parseInt(testpaperId));
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("findAllQuestionBank", findAllQuestionBank);
		modelAndView.addObject("selectIdTestPaperQuesion", selectIdTestPaperQuesion);
		modelAndView.setViewName("_admin/editItemList");
		return modelAndView;
	}

	//修改试卷试题
	@RequestMapping(value="/editTestPaperQuestion")
	@ResponseBody
	public String editTestPaperQuestion(@RequestBody TestPaperTestsList testPaperTestsList,HttpServletRequest request,HttpServletResponse response){
		int j=testPaperTestService.deleteTestPaperTestById(testPaperTestsList.getTestpaperId());
		int addTestPaperQuestion = testPaperTestService.addTestPaperQuestion(testPaperTestsList);
		System.out.println(addTestPaperQuestion);
		if(addTestPaperQuestion>=1){
			return "1";
		}else{
			return "0";
		}

	}

	@ResponseBody
	@RequestMapping(value = "/addtestpaper")
	public String  addTestPaper(TestPaper testPaper) {
    	/*testPaper.setTestpaperState(1);
    	int insertSelective = testPaperController.insertSelective(testPaper);
    	if (insertSelective>=1) {
			return true;
		}
    	return false;*/
		return randomPaper(testPaper);

	}

	/**
	 * 随机生成一份试卷
	 * @param testPaper
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/randomPaper")
	public String  randomPaper(TestPaper testPaper) {
		testPaper.setTestpaperState(1);
		int insertSelective = testPaperController.insertSelective(testPaper);

		List<TestPaper> testPaperList = testPaperController.findTestPaperInfo();

		List<QuestionBankVo> questionList = testPaperService.findAllQuestionBank();
		//创建一个选择题list
		List<QuestionBankVo> chooseQuestionList = new ArrayList<QuestionBankVo>();
		//创建一个判断题list
		List<QuestionBankVo> judgeQuestionList = new ArrayList<QuestionBankVo>();
		//把选择题和判断题分类存放
		for (int i = 0; i < questionList.size() ; i++) {
			int k = questionList.get(i).getTestsType();
			if(k==0){
				judgeQuestionList.add(questionList.get(i));
			}else{
				chooseQuestionList.add(questionList.get(i));
			}
		}
		//随机抽取不同的数量的选择和判断题
		List<QuestionBankVo> randomChooseQuestionList = RandomQuestion.getSubStringByRadom(chooseQuestionList,10);

		List<QuestionBankVo> randomJudgeQuestionList = RandomQuestion.getSubStringByRadom(judgeQuestionList,10);

		//创建一个存放试卷试题id的list,并把试卷试题id存到list里面
		List<Integer> questionBankId  = new ArrayList<>();
		for (QuestionBankVo questionBankVo :randomChooseQuestionList) {
			questionBankId.add(questionBankVo.getQuestionBankId());
		}

		for (QuestionBankVo questionBankVo :randomJudgeQuestionList) {
			questionBankId.add(questionBankVo.getQuestionBankId());
		}

		TestPaperTestsList testPaperTestsList = new TestPaperTestsList();

		testPaperTestsList.setQuestionBankId(questionBankId);

		//试卷的id为空怎么解决
		testPaperTestsList.setTestpaperId(testPaper.getTestpaperId());

		testPaperTestService.deleteTestPaperTestById(testPaperTestsList.getTestpaperId());

		int addTestPaperQuestion = testPaperTestService.addTestPaperQuestion(testPaperTestsList);

		if(addTestPaperQuestion>=1){
			return testPaperTestsList.getTestpaperId()+"";
		}else{
			return "0";
		}

	}
}
