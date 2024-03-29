
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import jxl.CellFormat;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bala
 */
public final class ReportPanel extends JPanel {

    private ReadingEntryPanel entryPanel;
    private final int w;
    private final int h;
    int reportTableHeight;
    double[] multFactors;
    // private JTable entryTable;

    public ReportPanel(ReadingEntryPanel entryPanel, int w, int h) {
        this.entryPanel = entryPanel;
        this.w = w;
        this.h = h - 25;
        go();
    }

    void go() {
//        try {


        double[][] given = parseEntryTable();
        multFactors = calculateMultFactors();
        double[][] data = this.findReportValues(given);
        this.addDeclaredValuesPanel();
        this.getAndAddTable(data);

        this.addSignature();
        this.addTitle();
        setLayout(null);
        setBounds(0, 0, w, h);
        this.setPreferredSize(new Dimension(1024,640));
        //getParent().validate();
  /*      } catch (Exception ex) {
        showErrorMessage();
        ex.printStackTrace();
        return;
        }*/
    }

    public int findRowCount() {
       
JTable entryTable = getEntryPanel().getTable();
        int i = 0;
        for (i = 0; i < 8; i++) {
             String str=null;
            try{
           str = (entryTable.getValueAt(i, 1)).toString();
            if (str.trim().length() == 0) {
                break;
            }
            
            }catch(Exception ex){ex.printStackTrace();break;}

      }  //System.out.println("Row count is " + i);
        return i;
    
    }
    public double[][] parseEntryTable() {
        //  try {
        int rows = findRowCount();
        int cols = 7;
        JTable entryTable = entryPanel.getTable();
        double[][] given = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                given[i][j] = Double.parseDouble((entryTable.getValueAt(i, j)).toString());
            }
        }
        return given;
        //  } catch (Exception ex) {
        //    ex.printStackTrace();
        //  return null;
        // }
    }

    public double[] calculateMultFactors() {
        double[] multFactors = new double[7];
        JTable entryTable = entryPanel.getTable();
        multFactors[0] = 0.0;
        for (int i = 1; i < 7; i++) {
            multFactors[i] = Double.parseDouble((entryTable.getValueAt(8, i)).toString());
        }
        return multFactors;
    }

    public double[][] findReportValues(double[][] given) {
        // try {
        int rows = findRowCount();

        int cols = 15;
        double diaDel = Double.parseDouble(entryPanel.getDelSizeField().getText());
        //   double diaSuction = Double.parseDouble(entryPanel.getSuctionSizeField().getText());


        double pipeConstant = 4000.0 * 4000.0 / (Math.PI * Math.PI * diaDel * diaDel * diaDel * diaDel * 2.0 * 9.81);
        // double pipeConstantSuction = 4000.0 * 4000.0 / (Math.PI * Math.PI * diaSuction * diaSuction * diaSuction
        //       * diaSuction * 2.0 * 9.81);
        //double pipeConstant = pipeConstantDel - pipeConstantSuction;
        // System.out.println("Pipe constant for mm diameter  is" + pipeConstant);
        double data[][] = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            double rater = 50.0 / given[i][EntryTableConstants.FREQ_COL_INDEX];
            data[i][ReportTableConstants.SLNO_COL_INDEX] = i + 1;
            data[i][ReportTableConstants.FREQ_COL_INDEX] = given[i][EntryTableConstants.FREQ_COL_INDEX]
                    * multFactors[EntryTableConstants.FREQ_COL_INDEX];
            //   data[i][ReportTableConstants.SGR_COL_INDEX] = given[i][EntryTableConstants.SGR_COL_INDEX]
            //          * multFactors[EntryTableConstants.SGR_COL_INDEX] * 0.0135;
            data[i][ReportTableConstants.DGR_COL_INDEX] = given[i][EntryTableConstants.DGR_COL_INDEX]
                    * multFactors[EntryTableConstants.DGR_COL_INDEX];
            double disch = given[i][EntryTableConstants.DISCH_COL_INDEX] * multFactors[EntryTableConstants.DISCH_COL_INDEX];
            data[i][ReportTableConstants.VHC_COL_INDEX] = disch * disch * pipeConstant;
            data[i][ReportTableConstants.TH_COL_INDEX] = given[i][ReportTableConstants.DGR_COL_INDEX]
                    + Double.parseDouble((getEntryPanel().getGaugDistField()).getText())
                    + data[i][ReportTableConstants.VHC_COL_INDEX];
            data[i][ReportTableConstants.DISCH_COL_INDEX] = given[i][EntryTableConstants.DISCH_COL_INDEX]
                    * multFactors[EntryTableConstants.DISCH_COL_INDEX];
            data[i][ReportTableConstants.VOL_COL_INDEX] = given[i][EntryTableConstants.VOL_COL_INDEX]
                    * multFactors[EntryTableConstants.VOL_COL_INDEX];

            data[i][ReportTableConstants.CURR_COL_INDEX] = given[i][EntryTableConstants.CURR_COL_INDEX]
                    * multFactors[EntryTableConstants.CURR_COL_INDEX];
            data[i][ReportTableConstants.MINPUT_COL_INDEX] = given[i][EntryTableConstants.POWER_COL_INDEX]
                    * multFactors[EntryTableConstants.POWER_COL_INDEX];
            data[i][ReportTableConstants.RDISCH_COL_INDEX] = rater * data[i][ReportTableConstants.DISCH_COL_INDEX];
            data[i][ReportTableConstants.RHEAD_COL_INDEX] = rater * rater * data[i][ReportTableConstants.TH_COL_INDEX];
            data[i][ReportTableConstants.RINPUT_COL_INDEX] = rater * rater * rater * data[i][ReportTableConstants.MINPUT_COL_INDEX];
            data[i][ReportTableConstants.POP_COL_INDEX] = data[i][ReportTableConstants.RDISCH_COL_INDEX]
                    * data[i][ReportTableConstants.RHEAD_COL_INDEX] / 102.00;
            data[i][ReportTableConstants.EFF_COL_INDEX] = data[i][ReportTableConstants.POP_COL_INDEX] / data[i][ReportTableConstants.RINPUT_COL_INDEX] * 100.00;

        }
        return data;
        // } catch (Exception ex) {
        //  ex.printStackTrace();
        //   showErrorMessage();
        // return null;
        //}

    }

    public void addTitle() {
        JPanel titlePanel = new JPanel() {

            public void paintComponent(Graphics g) {
                Graphics2D g2D = (Graphics2D) g;
                g2D.setFont(new Font("SansSerif", Font.PLAIN, 18));
                g2D.drawString("Kera Pump  Company,Coimbatore", 2, 25);
                g2D.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2D.drawString("Test Report of Openwell Submersible Pumpset, IS 14220", 2, 45);
            }
        };

        titlePanel.setBounds(0, 25, w, 75);
        add(titlePanel);
    }

    public void getAndAddTable(double[][] data) {


        Object[] columnNames = {"Sl.No", "Freq", "<HTML>Del.Gauge<BR>Reading", "VHC", "<HTML>Total<BR>Head</HTML>",
            "Disch", "Voltage", "Current", "<HTML>Motor<BR>Input",
            "<HTML>Rated<BR>Disch<HTML>", "<HTML>Rated<BR>Head</HTML>", "<HTML>Rated<BR>Input</HTML>", "<HTML>Rated<BR>Output</HTML>",
            "<HTML>Overall<BR>Eff.</HTML>"};
        //First Row is for units.So the total number of rows is result of findRowCount + 2;(findrowCounts is zero based);
        Object[][] dataObj = new Object[this.findRowCount() + 1][15];
        dataObj[0] = new Object[]{"", "Hz", "m", "m", "m", "lps", "V", "A", "kW", "lps", "m", "kW", "kW", "%"};
        for (int i = 1; i < findRowCount() + 1; i++) {
            for (int j = 0; j < 14; j++) {

                dataObj[i][j] = String.format("%,.2f", (float) (data[i - 1][j]));
                if (j == 0) {
                    dataObj[i][j] = String.format("%,.0f", (float) (data[i - 1][j]));
                }
            }
        }
        JTable reportTable = new JTable(dataObj, columnNames);
        TableCellRenderer rendererFromHeader = reportTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.LEFT); // Here you can set the alignment you want.
        reportTable.setBackground(Color.white);
        reportTable.setRowHeight(17);
        reportTable.setRowMargin(3);
        reportTableHeight = 40 + (this.findRowCount()) * 20;
        reportTable.getTableHeader().setPreferredSize(new Dimension(reportTable.getColumnModel().getTotalColumnWidth(), 40));
        reportTable.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 8));
        reportTable.getTableHeader().setBackground(Color.white);
        reportTable.setFont(new Font("SansSerif", Font.PLAIN, 8));
      //  reportTable.setBorder(BorderFactory.createLineBorder(Color.black));
        JScrollPane tScroller = new JScrollPane(reportTable);
        tScroller.setBounds(5, 270, w - 10, reportTableHeight);//trial and error
        //tScroller.setBorder(BorderFactory.createLineBorder(Color.black));
        add(tScroller);
    }

    public void generateExcelReport(GraphPanel gp) {
        try {
            WritableFont times = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD);
            WritableCellFormat wcf = new WritableCellFormat(times);

            File in = new File("resources\\xlReport.xls");
            File out = new File("resources\\xlReportFilled.xls");
            Workbook wb = Workbook.getWorkbook(in);
            WritableWorkbook copy = Workbook.createWorkbook(out, wb);
            WritableSheet sheet = copy.getSheet(0);
            Label lbl = new Label(4, 3, ": " + entryPanel.getPumpTypeField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(4, 4, ": " + entryPanel.getSlNoField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(4, 5, ": " + entryPanel.getDelSizeField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(4, 6, ": " + entryPanel.getHeadField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(4, 7, ": " + entryPanel.getDischField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(9, 3, ": " + entryPanel.gethRangeLwrField().getText()
                    + "/" + entryPanel.gethRangeUprField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(9, 4, ": " + entryPanel.getEffField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(9, 5, ": " + entryPanel.getRatingField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(9, 6, ": " + entryPanel.getVoltField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(9, 7, ": " + entryPanel.getCurrField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(14, 6, ": " + entryPanel.getGaugDistField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);




            double[][] given = parseEntryTable();
            double[][] data = this.findReportValues(given);

            for (int i = 0; i < findRowCount(); i++) {
                int colCount = 14;
                for (int j = 0; j < colCount; j++) {
                    int iNew = i + 16;
                    int jNew = j + 1;
                    //Label lbl;

                    if (j == 0) {
                        lbl = new Label(jNew, iNew, String.format("%,.0f", (float) (data[i][j])));

                        wcf = new WritableCellFormat(times);//lbl.getCellFormat());
                        wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
                        wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);

                    } else if (j == 9) {
                        lbl = new Label(jNew, iNew, String.format("%,.2f", (float) (data[i][11])));

                        wcf = new WritableCellFormat(times);
                        wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
                    } else if (j == 11) {
                        lbl = new Label(jNew, iNew, String.format("%,.2f", (float) (data[i][9])));
                        wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
                        wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
                    } else {
                        lbl = new Label(jNew, iNew, String.format("%,.2f", (float) (data[i][j])));
                        wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
                        wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
                    }
                    //top horizontal line of table
                    if (i == 0) {
                        wcf.setBorder(Border.TOP, BorderLineStyle.THIN);
                    }

                    //bottom horizontal line of table
                    if (i == (this.findRowCount() - 1)) {
                        wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
                    }
                    lbl.setCellFormat(wcf);
                    sheet.addCell(lbl);
                }
            }
//            final GraphPanel gp = new GraphPanel(new ReportPanel(entryPanel, w, h), w, h - 25);

            ArrayList<Renderer> rendererList = gp.getGraph().getPlot().getRendererList();
            for (Renderer renderer : rendererList) {
                renderer.setCurvePaint(Color.BLACK);
                renderer.setStroke(new BasicStroke(1.5f));
            }
            ArrayList<RangeAxis> rangeAxisList = gp.getGraph().getPlot().getRangeAxesList();
            Font old = null;
            for (RangeAxis rAxis : rangeAxisList) {
                rAxis.setAxisLinePaint(Color.BLACK);
                old = rAxis.getFont();
                rAxis.setFont(new Font(old.getFontName(), Font.BOLD, 12));
            }

            Plot p = gp.getGraph().getPlot();
            p.sethExrColor(Color.BLACK);
            Stroke s = new BasicStroke(1.0f);
            p.sethExrStroke(s);
            p.sethExrColor(Color.BLACK);
            p.seteExrStroke(s);
            p.seteExrColor(Color.BLACK);
            p.setcExrStroke(s);
            p.setcExrColor(Color.BLACK);


            gp.getGraph().getPlot().getDomainAxis().setAxisLineColor(Color.black);
            // Font old = gp.getGraph().getPlot().getDomainAxis().getFont();

            gp.getGraph().getPlot().getDomainAxis().setFont(new Font(old.getFontName(), Font.BOLD, 12));
            //gp.getGraph().getPlot().

            BufferedImage image = new BufferedImage(
                    w - 8, h - 120, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2D = (Graphics2D) image.getGraphics();
            g2D.scale(1.0, 1);


            gp.paint(g2D);
            File file = null;
            try {
                file = new File("GraphOut.png");
                ImageIO.write(image, "png", file);



            } catch (Exception ex) {
                ex.printStackTrace();
            }
            WritableImage wImage = new WritableImage(1, 25, 14, 19, file);
            sheet.addImage(wImage);
            PumpValues obs = gp.getGraph().getPlot().getObsValues();
            PumpValues decl = gp.declaredValues;
            lbl = new Label(1, 46, "Type : " + entryPanel.getPumpTypeField().getText());
            wcf = new WritableCellFormat(times);// new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            // wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(7, 46, entryPanel.getSlNoField().getText());
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(1, 47, "Head Range , mWC : " + entryPanel.gethRangeLwrField().getText() + " / " + entryPanel.gethRangeUprField().getText());
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            // wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = formatter.format(entryPanel.getDateChooser().getDate());
            lbl = new Label(1, 48, "Date : " + dateString);
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(10, 46, String.format("%.2f", decl.getDischarge()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);

            sheet.addCell(lbl);
            lbl = new Label(11, 46, String.format("%.2f", decl.getHead()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(13, 46, String.format("%.2f", decl.getEfficiency()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(14, 46, String.format("%.2f", decl.getMaxCurrent()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);


            lbl = new Label(10, 47, String.format("%.2f", obs.getDischarge()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(11, 47, String.format("%.2f", obs.getHead()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(13, 47, String.format("%.2f", obs.getEfficiency()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(14, 47, String.format("%.2f", obs.getMaxCurrent()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(10, 48, obs.getDischResult().toString());
            wcf = new WritableCellFormat(times);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(11, 48, obs.getHeadResult().toString());
            wcf = new WritableCellFormat(times);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(13, 48, obs.getEffResult().toString());
            wcf = new WritableCellFormat(times);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(14, 48, obs.getCurrResult().toString());
            wcf = new WritableCellFormat(times);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            copy.write();
            copy.close();
            String fileName = "resources\\xlReportFilled.xls";
            String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileName};//
            Runtime.getRuntime().exec(commands);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PumpValues getDeclaredValues() {
        PumpValues values = new PumpValues();
        values.setsNo(this.entryPanel.getSlNoField().getText().trim());
        values.setType(this.entryPanel.getPumpTypeField().getText());
        values.setDate(this.entryPanel.getDateChooser().getDate());
        values.setDischarge(Double.parseDouble(getEntryPanel().getDischField().getText()));
        values.setHead(Double.parseDouble(getEntryPanel().getHeadField().getText()));
        values.setEfficiency(Double.parseDouble(getEntryPanel().getEffField().getText()));
        values.setMaxCurrent(Double.parseDouble(getEntryPanel().getCurrField().getText()));
        values.setHeadRangeMax(Double.parseDouble(getEntryPanel().gethRangeUprField().getText()));
        values.setHeadRangeMin(Double.parseDouble(getEntryPanel().gethRangeLwrField().getText()));
        return values;
    }

    public MaxValuesForScale getValuesForScale() {

        MaxValuesForScale values = new MaxValuesForScale();
        values.setDischMax(Double.parseDouble(getEntryPanel().getDischMaxForScaleField().getText()));
        values.setHeadMax(Double.parseDouble(getEntryPanel().getHeadMaxForScaleField().getText()));
        values.setCurrMax(Double.parseDouble(getEntryPanel().getCurrMaxForScaleField().getText()));
        values.setEffMax(Double.parseDouble(getEntryPanel().getEffMaxForScaleField().getText()));
        return values;
    }

    public Dataset getDataset(DatasetAndCurveType type) {

        int rows = findRowCount();
        Dataset dataset = null;
        switch (type) {

            case DISCHARGE_VS_CURRENT: {

                double[] currents = new double[rows];
                double[] discharges = new double[rows];
                double[][] data = this.findReportValues(this.parseEntryTable());
                for (int i = 0; i < rows; i++) {
                    currents[i] = data[i][ReportTableConstants.CURR_COL_INDEX];
                    discharges[i] = data[i][ReportTableConstants.RDISCH_COL_INDEX];
                }
                dataset = new Dataset(discharges, currents, DatasetAndCurveType.DISCHARGE_VS_CURRENT);
                break;
            }

            case DISCHARGE_VS_EFFICIENCY: {

                double[] efficiencies = new double[rows];
                double[] discharges = new double[rows];
                double[][] data = this.findReportValues(this.parseEntryTable());
                for (int i = 0; i < rows; i++) {
                    efficiencies[i] = data[i][ReportTableConstants.EFF_COL_INDEX];
                    discharges[i] = data[i][ReportTableConstants.RDISCH_COL_INDEX];
                }
                dataset = new Dataset(discharges, efficiencies, DatasetAndCurveType.DISCHARGE_VS_EFFICIENCY);
                break;
            }

            case DISCHARGE_VS_HEAD: {

                double[] heads = new double[rows];
                double[] discharges = new double[rows];
                double[][] data = this.findReportValues(this.parseEntryTable());
                for (int i = 0; i < rows; i++) {
                    heads[i] = data[i][ReportTableConstants.RHEAD_COL_INDEX];
                    discharges[i] = data[i][ReportTableConstants.RDISCH_COL_INDEX];
                }
                dataset = new Dataset(discharges, heads, DatasetAndCurveType.DISCHARGE_VS_HEAD);
                break;
            }
        }
        return dataset;
    }

    /**
     * @return the entryPanel
     */
    public ReadingEntryPanel getEntryPanel() {
        return entryPanel;
    }

    private void addDeclaredValuesPanel() {
        JPanel valuesPanel = new JPanel();
        valuesPanel.setBounds(0, 75, w, 180);
        add(valuesPanel);
        valuesPanel.setBackground(Color.white);
        valuesPanel.setLayout(new GridLayout(4, 6));
        //JLabel title1 = new JLabel("Annai   Engg   ");
        //valuesPanel.add(title1);
        //JLabel title2 = new JLabel("  Company");
        //valuesPanel.add(title2);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 8);
        JLabel slNoLabel = new JLabel("Sl. No : ");
        slNoLabel.setFont(labelFont);
        valuesPanel.add(slNoLabel);
        JLabel enteredSlNoLabel = new JLabel(entryPanel.getSlNoField().getText());
        enteredSlNoLabel.setFont(labelFont);
        valuesPanel.add(enteredSlNoLabel);

        JLabel ipNoLabel = new JLabel("InPass No. : ");
        valuesPanel.add(ipNoLabel);
        ipNoLabel.setFont(labelFont);
        JLabel enteredIpNoLabel = new JLabel(entryPanel.getIpNoField().getText());
        valuesPanel.add(enteredIpNoLabel);
        enteredIpNoLabel.setFont(labelFont);
        JLabel dateLabel = new JLabel("Date : ");
        valuesPanel.add(dateLabel);
        dateLabel.setFont(labelFont);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(entryPanel.getDateChooser().getDate());
        JLabel enteredDateLabel = new JLabel(dateString);


        valuesPanel.add(enteredDateLabel);
        enteredDateLabel.setFont(labelFont);


        JLabel typeLabel = new JLabel("Pump Type : ");
        valuesPanel.add(typeLabel);
        typeLabel.setFont(labelFont);
        JLabel enteredTypeLabel = new JLabel(entryPanel.getPumpTypeField().getText());
        valuesPanel.add(enteredTypeLabel);
        enteredTypeLabel.setFont(labelFont);
        JLabel ratingLabel = new JLabel("Rating (kW/HP)     :");
        valuesPanel.add(ratingLabel);
        ratingLabel.setFont(labelFont);
        
        
        
        JLabel enteredRatingLabel = new JLabel(entryPanel.getRatingField().getText());
        valuesPanel.add(enteredRatingLabel);
        enteredRatingLabel.setFont(labelFont);
        
        
        
        
        
        JLabel headLabel = new JLabel("Total Head(m) : ");
        valuesPanel.add(headLabel);
        headLabel.setFont(labelFont);
        JLabel enteredHeadLabel = new JLabel(entryPanel.getHeadField().getText());
        valuesPanel.add(enteredHeadLabel);
        enteredHeadLabel.setFont(labelFont);
        JLabel dischLabel = new JLabel("Discharge (lps) : ");
        valuesPanel.add(dischLabel);
        dischLabel.setFont(labelFont);
        JLabel enteredDischLabel = new JLabel(entryPanel.getDischField().getText());
        valuesPanel.add(enteredDischLabel);
        enteredDischLabel.setFont(labelFont);

        JLabel effLabel = new JLabel("Overall Eff.(%) : ");
        valuesPanel.add(effLabel);
        effLabel.setFont(labelFont);
        JLabel enteredEffLabel = new JLabel(entryPanel.getEffField().getText());
        valuesPanel.add(enteredEffLabel);
        enteredEffLabel.setFont(labelFont);
        JLabel currLabel = new JLabel("Max.Current (A) : ");
        valuesPanel.add(currLabel);
        currLabel.setFont(labelFont);
        JLabel enteredCurrLabel = new JLabel(entryPanel.getCurrField().getText());
        valuesPanel.add(enteredCurrLabel);
        enteredCurrLabel.setFont(labelFont);
        JLabel headRangeLabel = new JLabel("Head Range (m) : ");
        valuesPanel.add(headRangeLabel);
        headRangeLabel.setFont(labelFont);
        JLabel enteredHeadRangeLabel = new JLabel(entryPanel.gethRangeLwrField().getText() + " / " + entryPanel.gethRangeUprField().getText());
        valuesPanel.add(enteredHeadRangeLabel);
        enteredHeadRangeLabel.setFont(labelFont);
        JLabel voltLabel = new JLabel("Voltage (V) : ");
        valuesPanel.add(voltLabel);
        voltLabel.setFont(labelFont);
        JLabel enteredVoltLabel = new JLabel(entryPanel.getVoltField().getText());
        valuesPanel.add(enteredVoltLabel);
        enteredVoltLabel.setFont(labelFont);
        JLabel phaseLabel = new JLabel("Phase  : ");
        valuesPanel.add(phaseLabel);
        phaseLabel.setFont(labelFont);
        JLabel enteredPhaseLabel = new JLabel(entryPanel.getPhaseField().getText());
        valuesPanel.add(enteredPhaseLabel);
        enteredPhaseLabel.setFont(labelFont);
        JLabel freqLabel = new JLabel("Frequency (Hz) : ");
        valuesPanel.add(freqLabel);
        freqLabel.setFont(labelFont);
        JLabel enteredFreqLabel = new JLabel(entryPanel.getFreqField().getText());
        valuesPanel.add(enteredFreqLabel);
        enteredFreqLabel.setFont(labelFont);
        // JLabel suctionSizeLabel = new JLabel("Suction Size(mm)  : ");
        //   valuesPanel.add(suctionSizeLabel);
        //  suctionSizeLabel.setFont(labelFont);
//        JLabel enteredSuctionSizeLabel = new JLabel(entryPanel.getSuctionSizeField().getText());
        //       valuesPanel.add(enteredSuctionSizeLabel);
        //     enteredSuctionSizeLabel.setFont(labelFont);




        JLabel delSizeLabel = new JLabel("Del. Size(mm)  : ");
        valuesPanel.add(delSizeLabel);
        delSizeLabel.setFont(labelFont);
        JLabel enteredDelSizeLabel = new JLabel(entryPanel.getDelSizeField().getText());
        valuesPanel.add(enteredDelSizeLabel);
        enteredDelSizeLabel.setFont(labelFont);
        JLabel gDistLabel = new JLabel("Gauge Distance (m) : ");
        valuesPanel.add(gDistLabel);
        gDistLabel.setFont(labelFont);
        JLabel enteredGDistLabel = new JLabel(entryPanel.getGaugDistField().getText());
        valuesPanel.add(enteredGDistLabel);
        enteredGDistLabel.setFont(labelFont);
        JLabel remarksLabel = new JLabel("Remarks : ");
        valuesPanel.add(remarksLabel);
        remarksLabel.setFont(labelFont);
        JLabel enteredRemarksLabel = new JLabel(entryPanel.getRemarksField().getText());
        valuesPanel.add(enteredRemarksLabel);
        enteredRemarksLabel.setFont(labelFont);

    }

    public void addSignature() {
        SignPanel signPanel = new SignPanel();

        signPanel.setBounds(0, h - 80, w, 80);
        add(signPanel);
    }

    class SignPanel extends JPanel {

        public void paintComponent(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            g2D.setFont(new Font("SansSerif", Font.PLAIN, 8));
            g2D.drawString("Casing Pressure Test : Casing withstood 1.5 times the max. discharge pressure for 2 mins.", 2, 10);
            g2D.drawString("Signature ", 2, 80);
        }
    }

    public static void showErrorMessage() {
        JFrame f = new JFrame();
        f.setBounds(400, 200, 600, 400);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);

        JOptionPane.showMessageDialog(f,
                "Enter only  numbers as values and ensure that no two discharge values are equal.");
        return;
    }
}
