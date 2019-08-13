package com.exam.util;

import com.exam.entity.Options;
import com.exam.entity.vo.QuestionBankVo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {
    //总行数  
    private int totalRows = 0;
    //总条数  
    private int totalCells = 0;
    //错误信息接收器  
    private String errorMsg;
    //构造方法  
    public ReadExcel(){}
    //获取总行数  
    public int getTotalRows(){
        return totalRows;
    }
    //获取总列数  
    public int getTotalCells(){
        return totalCells;
    }
    //获取错误信息  
    public String getErrorInfo(){
        return errorMsg;
    }


    /** 
    * 读EXCEL文件，获取信息集合
    * @return 
    */
    public List<QuestionBankVo> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();//获取文件名
        List<QuestionBankVo> QuestionVoList = null;
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }

            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }


            QuestionVoList = createExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return QuestionVoList;
    }
    /** 
    * 根据excel里面的内容读取客户信息 
    * @param is 输入流 
    * @param isExcel2003 excel是2003还是2007版本 
    * @return 
    * @throws IOException 
    */
    public List<QuestionBankVo> createExcel(InputStream is, boolean isExcel2003) {
        List<QuestionBankVo> QuestionVoList = null;
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            QuestionVoList = readExcelValue(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return QuestionVoList;
    }
            
          /** 
    * 读取Excel里面的信息
    * @param wb 
    * @return 
    */  
    private List<QuestionBankVo> readExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<QuestionBankVo> QuestionVoList = new ArrayList<QuestionBankVo>();


        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            QuestionBankVo questionVo = new QuestionBankVo();
            List<Options> options = new ArrayList<Options>();
            Options optionsA = new Options();
            Options optionsB = new Options();
            Options optionsC = new Options();
            Options optionsD = new Options();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                            String stem = String.valueOf(cell.getNumericCellValue());
                            questionVo.setStem(stem.substring(0, stem.length()-2>0?stem.length()-2:1));//stem
                        }else{
                            questionVo.setStem(cell.getStringCellValue());//stem
                        }
                    } else if (c == 1) {
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                            String testType = String.valueOf(cell.getNumericCellValue());
                            questionVo.setTestsType(Integer.valueOf(testType.substring(0, testType.length()-2>0?testType.length()-2:1)));
                        }else{
                            questionVo.setTestsType(Integer.valueOf(cell.getStringCellValue()));
                        }
                    }else if(c == 2){
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                            String contentA = String.valueOf(cell.getNumericCellValue());
                            optionsA.setContent(contentA.substring(0, contentA.length()-2>0?contentA.length()-2:1));
                            optionsA.setOp("A");
                            options.add(0,optionsA);
                        }else{
                            optionsA.setContent(cell.getStringCellValue());//setState
                            optionsA.setOp("A");
                            options.add(0,optionsA);
                        }
                    }else if(c == 3){
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            String contentB = String.valueOf(cell.getNumericCellValue());
                            optionsB.setContent(contentB.substring(0, contentB.length() - 2 > 0 ? contentB.length() - 2 : 1));
                            optionsB.setOp("B");
                            options.add(1, optionsB);
                        }else{
                            optionsB.setContent(cell.getStringCellValue());
                            optionsB.setOp("B");
                            options.add(1,optionsB);
                        }
                    }else if(c == 4){
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            String contentC = String.valueOf(cell.getNumericCellValue());
                            optionsC.setContent(contentC.substring(0, contentC.length() - 2 > 0 ? contentC.length() - 2 : 1));
                            optionsC.setOp("C");
                            options.add(2, optionsC);
                        }else{
                            optionsC.setContent(cell.getStringCellValue());
                            optionsC.setOp("C");
                            options.add(2,optionsC);
                        }
                    }else if(c == 5){
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            String contentD = String.valueOf(cell.getNumericCellValue());
                            optionsD.setContent(contentD.substring(0, contentD.length() - 2 > 0 ? contentD.length() - 2 : 1));
                            optionsD.setOp("D");
                            options.add(3, optionsD);
                        }else{
                            optionsD.setContent(cell.getStringCellValue());
                            optionsD.setOp("D");
                            options.add(3,optionsD);
                        }
                        questionVo.setOptions(options);
                    }else if(c == 6){
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                            String answer = String.valueOf(cell.getNumericCellValue());
                            questionVo.setAnswer(answer.substring(0, answer.length()-2>0?answer.length()-2:1));
                        }else{
                            questionVo.setAnswer(cell.getStringCellValue());
                        }
                    }
                }
            }
            // 添加到list
            //QuestionVoList.add(questionVo);
            QuestionVoList.add(r-1,questionVo);

        }
        return QuestionVoList;
    }
              
        /**
  * 验证EXCEL文件
  *
  * @param filePath
  * @return
  */
    public boolean validateExcel(String filePath){
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "The file type is error!Please import excel file";
            return false;
        }
        return true;
    }
    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath)  {
         return filePath.matches("^.+\\.(?i)(xls)$");
     }
    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath){
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
}