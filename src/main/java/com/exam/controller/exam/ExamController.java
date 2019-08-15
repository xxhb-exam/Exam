package com.exam.controller.exam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.exam.controller.admin.TestPaperController;
import com.exam.dao.TestPaperMapper;
import com.exam.entity.Users;
import com.exam.service.TestPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.exam.entity.TestPaper;
import com.exam.entity.vo.QuestionBankVo;
import com.exam.service.ExamService;

@Controller
@RequestMapping(value = "/exam")
public class ExamController {

	@Autowired
	private ExamService examService;
	@Autowired
	private TestPaperService testPaperService;
	@Autowired
	private TestPaperController testPaperController;


	/**
	 * 获取所有试卷
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();

		List<TestPaper> allTestPaper = examService.findAllTestPaper();
		modelAndView.addObject("allTestPaper", allTestPaper);

		modelAndView.setViewName("_exam/index");
		return modelAndView;
	}

	/**
	 * 返回试卷页面的方法
	 * @return
	 */
	@RequestMapping(value = "/exam.html", method = RequestMethod.GET)
	public ModelAndView exam( HttpSession session,HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		/*Date date = new Date();*/
		TestPaper testPaper = new TestPaper();
		//获取用户对象
		Users users = (Users) session.getAttribute("myUser");
		Date date = new Date();
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddHHmmss");
		testPaper.setTestpaperName(users.getUserName()+dateFormat.format(date));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		testPaper.setStartDate(	simpleDateFormat.format(System.currentTimeMillis()));
		testPaper.setEndDate(simpleDateFormat.format(System.currentTimeMillis() + 60*60*1000));
		/*testPaper.setStartDate(System.currentTimeMillis()+"");
		testPaper.setEndDate(System.currentTimeMillis()+"60*60*1000");*/
		String id = testPaperController.randomPaper(testPaper);
		examService.findJudgmentQuestionAndChoiceQuestion(modelAndView, id, session);
/*		List<TestPaper> allTestPaper = examService.findAllTestPaper();
		modelAndView.addObject("allTestPaper", allTestPaper);
*/
		return modelAndView;
	}


	@ResponseBody
	@RequestMapping(value = "/submitpapers", method = RequestMethod.POST)
	public List<QuestionBankVo> submitPapers(@RequestBody List<QuestionBankVo> questionBankVos, HttpSession session) {

		List<QuestionBankVo> judgmentSystem = examService.JudgmentSystem(questionBankVos, session);

		return judgmentSystem;
	}

	/**
	 * 试卷模板映射
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/autoGenerate", method = RequestMethod.POST)
	public boolean autoGenerate(HttpSession session, TestPaper testPaper) {

		boolean autoGenerate = examService.autoGenerate(session, testPaper.getTestpaperId());

		return autoGenerate;
	}

}
