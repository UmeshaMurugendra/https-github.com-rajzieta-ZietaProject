package com.zieta.tms.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import com.zieta.tms.dto.TimeSheetReportDTO;
import com.zieta.tms.dto.TimeSheetSumReportDTO;
import com.zieta.tms.util.ReportUtil;


@Component
public class TimeSheetReportHelper {
	
	
	
	private Workbook workbook;
    private Sheet sheet;
     
  
 
    private void writeHeaderLine() {
    	workbook = new SXSSFWorkbook();
        sheet = workbook.createSheet("TimeSheet");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
//        font.setBold(true);
        font.setFontHeightInPoints((short)13);
        style.setFont(font);
        

         
        createCell(row, 0, "EMP_ID", style);    
        createCell(row, 1, "TEAM_ID", style); 
        createCell(row, 2, "TEAM", style);       
        createCell(row, 3, "EMP_NAME", style);    
        createCell(row, 4, "PROJECT_ID", style); 
        createCell(row, 5, "PROJECT_NAME", style);
        createCell(row, 6, "TASK_NAME", style);
        createCell(row, 7, "ACTIVITY_DESC", style);
        createCell(row, 8, "PLANNED_HOURS", style);
        createCell(row, 9, "ACTUAL_HOURS", style);
        createCell(row, 10, "TS_DATE", style);
        createCell(row, 11, "SUBMIT_DATE", style);
        createCell(row, 12, "SUBMITTED_HOURS", style);
        createCell(row, 13, "APPROVED_HOURS", style);
        
         
    }
     
    public  void createCell(Row row, int columnCount, Object value, CellStyle style) {
     //   DecimalFormat df = new DecimalFormat("#.00");
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines(List<TimeSheetReportDTO> tsReportList) {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeight((short)10);
        style.setFont(font);
                 
        CellStyle style2= ReportUtil.formatDecimalStyle(workbook);
        font.setFontHeight((short)14);
        style2.setFont(font);
        
        for (TimeSheetReportDTO timeSheetReport : tsReportList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, timeSheetReport.getEmp_id(), style);
            createCell(row, columnCount++, timeSheetReport.getTeam_id(), style);
            createCell(row, columnCount++, timeSheetReport.getTeam(), style);
            createCell(row, columnCount++, timeSheetReport.getEmp_name(), style);
            createCell(row, columnCount++, timeSheetReport.getProject_id(), style);
            createCell(row, columnCount++, timeSheetReport.getProject_name(), style);
            createCell(row, columnCount++, timeSheetReport.getTask_name(), style);
            createCell(row, columnCount++, timeSheetReport.getActivity_desc(), style);
            createCell(row, columnCount++, timeSheetReport.getPlanned_hours(), style);
            createCell(row, columnCount++, timeSheetReport.getActual_hours(), style);
            createCell(row, columnCount++, timeSheetReport.getTs_date(), style);
            createCell(row, columnCount++, timeSheetReport.getSubmit_date(), style);
            createCell(row, columnCount++, timeSheetReport.getSubmitted_hours(), style);
            createCell(row, columnCount++, timeSheetReport.getApproved_hours(), style);
             
        }
    }
     
    public ByteArrayInputStream downloadReport(HttpServletResponse response, List<TimeSheetReportDTO> tsReportList) throws IOException {
        writeHeaderLine();
        writeDataLines(tsReportList);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
         
    } 
    /////////////////////////////////////////////////////////////////////////////////
    
    private void writeSumHeaderLine() {
    	workbook = new SXSSFWorkbook(1000);
        sheet = workbook.createSheet("TimeSheet");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)13);
        style.setFont(font);
        

         
        createCell(row, 0, "EMP_ID", style);    
        createCell(row, 1, "TEAM_ID", style); 
        createCell(row, 2, "TEAM", style);       
        createCell(row, 3, "EMP_NAME", style);    
        createCell(row, 4, "PROJECT_ID", style); 
        createCell(row, 5, "PROJECT_NAME", style);
        createCell(row, 6, "TID", style);
        createCell(row, 7, "TASK_NAME", style);
        createCell(row, 8, "ACTIVITY", style);
        createCell(row, 9, "SUBMITTED_HOURS", style);
        createCell(row, 10, "APPROVED_HOURS", style);
        createCell(row, 11, "TOTAL PLANNED HOURS", style);
        createCell(row, 12, "TOTAL ACTUAL HOURS", style);
     //   createCell(row, 12, "SUBMITTED_HOURS", style);
     //   createCell(row, 13, "APPROVED_HOURS", style);
        
         
    }
    
    
    
    private void writeSumDataLines(List<TimeSheetSumReportDTO> tsReportList) {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)5);
        style.setFont(font);
                 
        CellStyle style2= ReportUtil.formatDecimalStyle(workbook);
        font.setFontHeightInPoints((short)14);
        style2.setFont(font);
        
        for (TimeSheetSumReportDTO timeSheetReport : tsReportList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, timeSheetReport.getEmp_id(), style);
            createCell(row, columnCount++, timeSheetReport.getTeam_id(), style);
            createCell(row, columnCount++, timeSheetReport.getTeam(), style);
            createCell(row, columnCount++, timeSheetReport.getEmployee_name(), style);
            createCell(row, columnCount++, timeSheetReport.getProj_id(), style);
            createCell(row, columnCount++, timeSheetReport.getProject_name(), style);
            createCell(row, columnCount++, timeSheetReport.getTid(), style);
            createCell(row, columnCount++, timeSheetReport.getTask_name(), style);
            createCell(row, columnCount++, timeSheetReport.getActivity(), style);
            createCell(row, columnCount++, timeSheetReport.getSubmitted_hrs(), style);
            createCell(row, columnCount++, timeSheetReport.getApproved_hrs(), style);
          //  createCell(row, columnCount++, timeSheetReport.getTs_date(), style);
          //  createCell(row, columnCount++, timeSheetReport.getSubmit_date(), style);
            createCell(row, columnCount++, timeSheetReport.getTotal_planned_hrs(), style);
            createCell(row, columnCount++, timeSheetReport.getTotal_actual_hrs(), style);
             
        }
    }
     
    public ByteArrayInputStream downloadSumReport(HttpServletResponse response, List<TimeSheetSumReportDTO> tsReportList) throws IOException {
        writeSumHeaderLine();
        writeSumDataLines(tsReportList);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
         
    } 
    

}
