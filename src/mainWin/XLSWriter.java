package mainWin;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;

import static mainWin.Main.writingInProcess;

public class XLSWriter implements Runnable {
    public static File file;
    public static boolean append = true;

    String name, id, dept, email, phone;

    public XLSWriter(String name, String id, String dept, String email, String phone) {
        this.name = name;
        this.id = id;
        this.dept = dept;
        this.email = email;
        this.phone = phone;

    }

    @Override
    public void run() {
        try {
            FileInputStream fin;
            FileOutputStream fout;
            HSSFWorkbook workbook;
            HSSFSheet worksheet;

            boolean created = false;

            if (file.exists()) {
                System.out.println("found");
                fin = new FileInputStream(file.toString());
                fout = new FileOutputStream(file.toString(), append);
                if(append) {
                    workbook = new HSSFWorkbook(new POIFSFileSystem(fin));
                    worksheet = workbook.getSheet("Registered");
                    System.out.println("append true");
                } else {
                    workbook = new HSSFWorkbook();
                    worksheet = workbook.createSheet("Registered");

                    HSSFRow rowL = worksheet.createRow((short) 0);

                    HSSFCellStyle style = workbook.createCellStyle();
                    HSSFFont font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);

                    HSSFCell cellA0 = rowL.createCell((short) 0);
                    cellA0.setCellValue("Name");

                    cellA0.setCellStyle(style);

                    HSSFCell cellA1 = rowL.createCell((short) 1);
                    cellA1.setCellValue("ID");
                    cellA1.setCellStyle(style);

                    HSSFCell cellA2 = rowL.createCell((short) 2);
                    cellA2.setCellValue("Department");
                    cellA2.setCellStyle(style);

                    HSSFCell cellA3 = rowL.createCell((short) 3);
                    cellA3.setCellValue("Email");
                    cellA3.setCellStyle(style);

                    HSSFCell cellA4 = rowL.createCell((short) 4);
                    cellA4.setCellValue("Phone");
                    cellA4.setCellStyle(style);

                    HSSFCell cellA5 = rowL.createCell((short) 10);
                    cellA5.setCellValue("Count");


                    cellA5 = rowL.createCell((short) 11);
                    cellA5.setCellValue(1);
                    created = true;
                }
            } else {
                System.out.println("create");
                fout = new FileOutputStream(file.toString());
                workbook = new HSSFWorkbook();
                worksheet = workbook.createSheet("Registered");

                HSSFRow rowL = worksheet.createRow((short) 0);

                HSSFCellStyle style = workbook.createCellStyle();
                HSSFFont font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);

                HSSFCell cellA0 = rowL.createCell((short) 0);
                cellA0.setCellValue("Name");

                cellA0.setCellStyle(style);

                HSSFCell cellA1 = rowL.createCell((short) 1);
                cellA1.setCellValue("ID");
                cellA1.setCellStyle(style);

                HSSFCell cellA2 = rowL.createCell((short) 2);
                cellA2.setCellValue("Department");
                cellA2.setCellStyle(style);

                HSSFCell cellA3 = rowL.createCell((short) 3);
                cellA3.setCellValue("Email");
                cellA3.setCellStyle(style);

                HSSFCell cellA4 = rowL.createCell((short) 4);
                cellA4.setCellValue("Phone");
                cellA4.setCellStyle(style);

                HSSFCell cellA5 = rowL.createCell((short) 10);
                cellA5.setCellValue("Count");


                cellA5 = rowL.createCell((short) 11);
                cellA5.setCellValue(1);

                created = true;
            }

            if (!created) {
                HSSFCell cell = worksheet.getRow(0).getCell(11);
                cell.setCellValue(cell.getNumericCellValue() + 1);
            }

            HSSFRow row1 = worksheet.createRow((short) ((created) ? 1 : worksheet.getLastRowNum() + 1));


            HSSFCell cellA1 = row1.createCell((short) 0);
            cellA1.setCellValue(name);

            HSSFCell cellB1 = row1.createCell((short) 1);
            cellB1.setCellValue(id);

            HSSFCell cellC1 = row1.createCell((short) 2);
            cellC1.setCellValue(dept);

            HSSFCell cellD1 = row1.createCell((short) 3);
            cellD1.setCellValue(email);

            HSSFCell cellE1 = row1.createCell((short) 4);
            cellE1.setCellValue(phone);

            for (int i = 0; i <= 5; i++) {
                worksheet.autoSizeColumn(i);
            }

            workbook.write(file);
            fout.close();
            workbook.close();
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writingInProcess = false;
        }
    }
}
