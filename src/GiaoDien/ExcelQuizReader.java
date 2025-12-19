package GiaoDien;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ExcelQuizReader {

    public static Map<String, String> quizInfo = new HashMap<>();
    public static List<Question> questions = new ArrayList<>();

    public static void read(String path, DefaultTableModel model) {

        try (FileInputStream fis = new FileInputStream(path)) {

            Workbook wb = WorkbookFactory.create(fis);
            DataFormatter formatter = new DataFormatter();

            /* ===== QUIZ INFO ===== */
            quizInfo.clear();
            Sheet info = wb.getSheet("QuizInfo");

            if (info != null) {
                for (int i = 0; i <= info.getLastRowNum(); i++) {

                    Row r = info.getRow(i);
                    if (r == null) continue;

                    String key = formatter.formatCellValue(r.getCell(0)).trim();
                    String value = formatter.formatCellValue(r.getCell(1)).trim();

                    if (!key.isEmpty()) {
                        quizInfo.put(key, value);
                    }
                }
            }

            /* ===== QUESTIONS ===== */
            questions.clear();
            model.setRowCount(0);

            Sheet qs = wb.getSheet("Questions");
            if (qs == null) {
                throw new RuntimeException("Không tìm thấy sheet 'Questions'");
            }

            for (int i = 1; i <= qs.getLastRowNum(); i++) {

                Row r = qs.getRow(i);
                if (r == null) continue;

                Question q = new Question(
                        formatter.formatCellValue(r.getCell(1)),
                        formatter.formatCellValue(r.getCell(2)),
                        formatter.formatCellValue(r.getCell(3)),
                        formatter.formatCellValue(r.getCell(4)),
                        formatter.formatCellValue(r.getCell(5)),
                        formatter.formatCellValue(r.getCell(6))
                );

                questions.add(q);

                model.addRow(new Object[]{
                        i,
                        q.content,
                        q.a,
                        q.b,
                        q.c,
                        q.d,
                        q.correct
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Lỗi đọc file Excel!\n" + e.getMessage(),
                "Excel Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
}
