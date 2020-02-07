package com.company;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.w3c.dom.ls.LSOutput;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }


        TaskGiverWindow tgw=new TaskGiverWindow();

        tgw.jb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int w = 595 * 1;
                            int h = 832 * 1;
                            tgw.printLine("Генерация работы...");
                            tgw.printLine("    *Формат страницы: A4");
                            tgw.printLine("    *Размер одного изображения: "+w+"x"+h+" пикселей");
                            tgw.printLine("    *Формат файла: PNG");
                            //Scanner s = new Scanner(System.in);
                            //tgw.printLine("Имя ученика");
                            String username = tgw.name.getText();
                            tgw.printLine("    *Папка с работой: "+username);
                            File directory = new File("" + username);
                            if (! directory.exists()) {
                                tgw.printLine("Создание папки...");
                                //tgw.printLine("ОК!");
                                directory.mkdir();
                            }else{
                                tgw.printLine("    *Найдена старая работа с таким же названием");
                                tgw.printLine("Удаление старых данных...");
                                deleteDirectoryRecursion(directory.toPath());
                                //tgw.printLine("ОК!");
                                tgw.printLine("Создание папки...");
                                directory.mkdir();
                                //tgw.printLine("ОК!");
                            }

                            Document document = new Document();
                            document.setPageSize(new com.itextpdf.text.Rectangle(w+0,h+0));
                            document.setMargins(0,0,0,0);
                            PdfWriter.getInstance(document, new FileOutputStream(username+"/"+username+".pdf"));
                            document.open();

                            String type = tgw.folder.getText();
                            tgw.printLine("    *Папка с заданиями: "+type);
                            int amount = (int) tgw.amount.getValue();
                            tgw.printLine("    *Количество задач: "+amount);
                            File dir = new File(type);
                            //File[] filesArray = dir.listFiles();
                            Collection<File> filesCol = FileUtils.listFiles(
                                    dir,
                                    FileFileFilter.FILE,
                                    DirectoryFileFilter.DIRECTORY
                            );
                            ArrayList<File> files=new ArrayList<>();
                            files.addAll(filesCol);

                            //for (int i = 0; i < filesArray.length; i++) {
                            //    files.add(filesArray[i]);
                            //}
                            if (amount > files.size()) {
                                tgw.printLine("    !Это больше, чем доступное количество задач. Укажите новое значение и попробуйте снова.");
                                tgw.printLine("    *В папке найдено задач: "+files.size());
                                return;
                            }
                            tgw.printLine("    *В папке найдено задач: "+files.size());
                            //tgw.printLine("    *Для создания работы будет использован следующий код: "+type.hashCode());
                            Random r = new Random();//type.hashCode());
                            HashSet<Integer> ts = new HashSet<>();

                            tgw.printLine("Формирование работы...");
                            while (ts.size() < amount) {
                                int tr=r.nextInt(files.size());
                                ts.add(tr);
                            }
                            //tgw.printLine("ОК!");
                            int curpos = 0;
                            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2d = (Graphics2D) bi.getGraphics();
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setFont(new Font("SansSerif", Font.PLAIN, 17*1));
                            g2d.setPaint(new Color(255, 255, 255));
                            g2d.fillRect(0, 0, w, h);
                            String result = username + System.lineSeparator();
                            tgw.printLine("Начало записи...");
                            int iter = 1;
                            int taskNumber=1;
                            for (int t : ts) {
                                tgw.printLine("Добавление задания "+files.get(t).getName()+"...");
                                BufferedImage task = ImageIO.read(files.get(t));
                                double k = (double) (w) / (double) (task.getWidth());
                                task = bwresize(task, (int) (task.getWidth() * k), (int) (task.getHeight() * k));
                                result += taskNumber+". "+files.get(t).getName() + System.lineSeparator();
                                if (curpos + task.getHeight() > bi.getHeight()) {
                                    tgw.printLine("Сохранение " + iter + " страницы ...");
                                    ImageIO.write(bi, "png", new File("" + username + "/" + username + ", лист №" + iter + ".png"));
                                    document.add(com.itextpdf.text.Image.getInstance( new File("" + username + "/" + username + ", лист №" + iter + ".png").getAbsolutePath()));
                                    bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                                    g2d = (Graphics2D) bi.getGraphics();
                                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                    g2d.setFont(new Font("SansSerif", Font.PLAIN, 17*1));
                                    g2d.setPaint(new Color(255, 255, 255));
                                    g2d.fillRect(0, 0, w, h);
                                    curpos = 0;
                                    iter++;
                                    //tgw.printLine("ОК!");
                                }
                                g2d.drawImage(task, 0, curpos, null);
                                g2d.setColor(new Color(0,0,0));
                                g2d.fillRect(0,curpos-1,w,2);
                                g2d.drawString("№"+taskNumber,0,curpos+20);
                                curpos += task.getHeight();
                                taskNumber++;
                                //tgw.printLine("ОК!");
                            }
                            //tgw.printLine("ОК!");
                            tgw.printLine("Сохранение "+iter+" страницы ...");
                            ImageIO.write(bi, "png", new File("" + username + "/" + username + ", лист №" + iter + ".png"));
                            document.add(com.itextpdf.text.Image.getInstance( new File("" + username + "/" + username + ", лист №" + iter + ".png").getAbsolutePath()));
                            //tgw.printLine("ОК!");
                            document.close();
                            tgw.printLine("Сохранение файла с описанием задания...");
                            tgw.printLine("    *Название файла описания: "+ username + ".txt");
                            BufferedWriter bw = new BufferedWriter(new FileWriter("" + username + "/" + username + ".txt"));
                            bw.write(result);
                            bw.close();
                            tgw.printLine("Отправка файла с ответами на сервак Альберта... (У кого-то будет пять)");

                            URL url = new URL("https://math-task-checker.herokuapp.com/addVariant/");
                            URLConnection con = url.openConnection();
                            HttpURLConnection http = (HttpURLConnection)con;
                            http.setRequestMethod("POST"); // PUT is another valid option
                            http.setDoOutput(true);
                            Map<String,String> arguments = new HashMap<>();
                            arguments.put("value", result);
                            StringJoiner sj = new StringJoiner("&");
                            for(Map.Entry<String,String> entry : arguments.entrySet())
                                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
                            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                            int length = out.length;http.setFixedLengthStreamingMode(length);
                            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                            http.connect();
                            try(OutputStream os = http.getOutputStream()) {
                                os.write(out);
                                os.flush();
                            }

                            //tgw.printLine("ОК!");
                            tgw.printLine("ОК!");
                        } catch (Exception ex) {
                            tgw.printLine("     !Произошла ошибка. Убедитесь, что правильно ввели названия папок и попробуйте снова.");
                            ex.printStackTrace();
                            tgw.printLine("Ошибка!");
                        }
                    }
                }).start();
            }
        });
    }

    public static BufferedImage bwresize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    static void deleteDirectoryRecursion(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursion(entry);
                }
            }
        }
        Files.delete(path);
    }
}
