package GiaoDien;

import org.apache.poi.ss.usermodel.*;
import java.io.*;
import java.util.*;

public class ExcelHelperXoaQuiz {

    public static List<String> readQuizNames(String path) throws Exception {
        List<String> list = new ArrayList<>();

        FileInputStream fis = new FileInputStream(path);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(0);

        DataFormatter formatter = new DataFormatter(); // lấy mọi formatt

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // bỏ header

            Cell cell = row.getCell(1); // CỘT B – Tên bộ câu hỏi
            if (cell == null) continue;

            String name = formatter.formatCellValue(cell).trim();

            if (!name.isEmpty()) {
                list.add(name);
            }
        }

        wb.close();
        fis.close();
        return list;
    }
}
