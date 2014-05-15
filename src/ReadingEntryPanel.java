
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bala
 */
public final class ReadingEntryPanel extends JPanel implements Serializable {

    private static class NumericTableModel extends DefaultTableModel{

        public NumericTableModel() {
        }
        @Override
        public Class getColumnClass(int col){
            return Double.class;
            
        }
    }

    private GridLinesType gridLinesType;
    private double desiredSmoothnessPercentage;
    protected JTextField slNoField;
    private JTextField ipNoField;
    protected JTextField pumpTypeField;
    private JTextField ratingField;
    private JTextField headField;
    private JTextField dischField;
    private JTextField effField;
    private JTextField currField;
    private JTextField voltField;
    private JTextField phaseField;
    private JTextField freqField;
    private JTextField hRangeLwrField;
    private JTextField hRangeUprField;
    private JTextField delSizeField;
    private JTextField gaugDistField;
    private JTextField isRefField;
    private JTextField remarksField;
    //  private JTextField dateStringField;
    JTable table;
    JPanel fieldsPanel;
    protected JDateChooser dateChooser;
    static final long serialVersionUID = -1881500357011727315L;
    private JTextField dischMaxForScaleField;
    private JTextField headMaxForScaleField;
    private JTextField effMaxForScaleField;
    private JTextField currMaxForScaleField;

    public ReadingEntryPanel() {
        this.setLayout(null);
        this.setBounds(0, 10, 1024, 768);
        this.setPreferredSize(new Dimension(1024,640));
        this.addFieldsPanel();this.setPreferredSize(new Dimension(1024,640));
        setBackground(new Color(236, 233, 216));
        this.addTable();
        this.addScalePanel();
        JLabel text = new JLabel("Maximum axis   values  to  use ;    Leave as  0 for  auto-scale  : ");

        text.setBounds(20, 276, 984, 24);
        this.add(text);

    }

    public void addFieldsPanel() {
        //JPanel upperPanel = new JPanel();
        //upperPanel.setLayout(null);
        InputVerifier v=    new NumericVerifier();
        fieldsPanel = new JPanel();
        //fieldsPanel(setBounds)
        fieldsPanel.setLayout(new GridLayout(10, 4, 25, 5));
        fieldsPanel.setBackground(new Color(236, 233, 216));
        JLabel slNoLabel = new JLabel("Sl. No : ");
        fieldsPanel.add(slNoLabel);
        slNoField = new JTextField(" ");
        fieldsPanel.add(slNoField);
        JLabel dateLabel = new JLabel("Date : ");
        fieldsPanel.add(dateLabel);
        dateChooser = new JDateChooser(new Date(), "dd-MM-yyyy");

        fieldsPanel.add(dateChooser);
        //   dateStringField = new JTextField("11.01.2011 ");
        // fieldsPanel.add(dateStringField);
        JLabel ipNoLabel = new JLabel("Inpass No : ");
        fieldsPanel.add(ipNoLabel);
        ipNoField = new JTextField("");
        fieldsPanel.add(ipNoField);
        JLabel pumpTypeLabel = new JLabel("Pump Type : ");
        fieldsPanel.add(pumpTypeLabel);
        pumpTypeField = new JTextField("");
        fieldsPanel.add(pumpTypeField);
        JLabel ratingLabel = new JLabel("Motor Rating(kW/HP) :   ");
        fieldsPanel.add(ratingLabel);
        ratingField = new JTextField(" ");
        fieldsPanel.add(ratingField);
        JLabel headLabel = new JLabel("Head (m) : ");
        fieldsPanel.add(headLabel);
        headField = new JTextField("");
        headField.setInputVerifier(v);
        fieldsPanel.add(headField);
        JLabel dischLabel = new JLabel("Discharge (lps) : ");
        fieldsPanel.add(dischLabel);
        dischField = new JTextField("");
        dischField.setInputVerifier(v);
        fieldsPanel.add(dischField);
        JLabel effLabel = new JLabel("Overall Eff. (%) : ");
        fieldsPanel.add(effLabel);
        effField = new JTextField("");
        effField.setInputVerifier(v);
        fieldsPanel.add(effField);
        JLabel currLabel = new JLabel("Max.current(A) : ");
        fieldsPanel.add(currLabel);
        currField = new JTextField("");
        currField.setInputVerifier(v);
        fieldsPanel.add(currField);
        JLabel voltLabel = new JLabel("Voltage(V)  : ");
        fieldsPanel.add(voltLabel);
        voltField = new JTextField("");
        voltField.setInputVerifier(v);
        fieldsPanel.add(voltField);
        JLabel phaseLabel = new JLabel("Phase  : ");
        fieldsPanel.add(phaseLabel);
        phaseField = new JTextField(" 1 ");
        phaseField.setInputVerifier(v);
        fieldsPanel.add(phaseField);
        JLabel freqLabel = new JLabel("Frequency(Hz)  : ");
        fieldsPanel.add(freqLabel);
        freqField = new JTextField("50.0");
        freqField.setInputVerifier(v);
        fieldsPanel.add(freqField);



        JLabel hRangeLwrLabel = new JLabel("Head Range Lower(m) : ");
        fieldsPanel.add(hRangeLwrLabel);
        hRangeLwrField = new JTextField(" ");
        hRangeLwrField.setInputVerifier(v);
        fieldsPanel.add(hRangeLwrField);
        JLabel hRangeUprLabel = new JLabel("Head Range Upper(m)  : ");
        fieldsPanel.add(hRangeUprLabel);
        hRangeUprField = new JTextField(" ");
        hRangeUprField.setInputVerifier(v);
        fieldsPanel.add(hRangeUprField);
        JLabel delSizeLabel = new JLabel("Delivery size(mm) : ");
        fieldsPanel.add(delSizeLabel);
        delSizeField = new JTextField(" ");
        delSizeField.setInputVerifier(v);
        fieldsPanel.add(delSizeField);
        JLabel gaugDistLabel = new JLabel("Gauge Distance(m)  : ");
        fieldsPanel.add(gaugDistLabel);
        gaugDistField = new JTextField(" ");
        gaugDistField.setInputVerifier(v);
        fieldsPanel.add(gaugDistField);
        JLabel isRefLabel = new JLabel("IS Ref  : ");
        fieldsPanel.add(isRefLabel);
        isRefField = new JTextField("");
        isRefField.setText("IS 14220");
        fieldsPanel.add(isRefField);

        JLabel remarksLabel = new JLabel("Remarks  : ");
        fieldsPanel.add(remarksLabel);
        remarksField = new JTextField(" ");
        fieldsPanel.add(remarksField);
        fieldsPanel.setBounds(20, 0, 984, 276);
        add(fieldsPanel);
        fieldsPanel.setVisible(true);
        setVisible(true);

    }

    public void addScalePanel() {
        InputVerifier v = new NumericVerifier();
        JPanel scalePanel = new JPanel();
        scalePanel.setLayout(new GridLayout(1, 8));
        scalePanel.setBackground(new Color(236, 233, 216));
        JLabel dischMaxLabel = new JLabel("Max.Disch(lps) : ");
        scalePanel.add(dischMaxLabel);
        dischMaxForScaleField = new JTextField("0.0");
        dischMaxForScaleField.setInputVerifier(v);
        scalePanel.add(dischMaxForScaleField);

        JLabel headMaxLabel = new JLabel("    Max.Head(m) : ");
        scalePanel.add(headMaxLabel);
        headMaxForScaleField = new JTextField("0.0");
          headMaxForScaleField.setInputVerifier(v);
        scalePanel.add(headMaxForScaleField);

        JLabel EffMaxLabel = new JLabel("   Max.Eff(%) : ");
        scalePanel.add(EffMaxLabel);
        effMaxForScaleField = new JTextField("0.0");
          effMaxForScaleField.setInputVerifier(v);
        scalePanel.add(effMaxForScaleField);
        JLabel currMaxLabel = new JLabel("          Max. Curr(A) : ");
        scalePanel.add(currMaxLabel);
        currMaxForScaleField = new JTextField("0.0");
         currMaxForScaleField.setInputVerifier(v);
        scalePanel.add(currMaxForScaleField);
        scalePanel.setBounds(20, 300, 984, 25);
        add(scalePanel);
        scalePanel.setVisible(true);
        setVisible(true);
    }

    public void addTable() {

       // DefaultTableModel model = new DefaultTableModel();
       NumericTableModel model = new NumericTableModel();
        table = new JTable(model);
        table.setBackground(new Color(236, 233, 216));
        model.addColumn("No.");
        model.addColumn("Frequency in Hz");
        model.addColumn("Gauge Reading in m");
        model.addColumn("Discharge in lps");
        model.addColumn("Voltage in V");
        model.addColumn("Current in A");
        model.addColumn("Power in kW");

        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 24));
        table.setRowHeight(24);
        table.setRowMargin(3);

        for (int k = 1; k < 9; k++) {
            Object[] rowData = {k, " ", " ", " ", " ", " ", " "};
            model.addRow(rowData);
        }
        /* Object[] rowData0 =  {"1 ", "50.00 ", "34.5 ", "0.00 ", "220 ", "7.30 ", "1.60 "};
        model.addRow(rowData0);
        Object[] rowData1 =  {"1 ", "50.20 ", "33.06 ", "0.55 ", "220 ", "7.60 ", "1.69 "};
        model.addRow(rowData1);
        Object[] rowData2 = {"1 ", "50.20 ", "28.20 ", "0.98", "220 ", "7.86 ", "1.76 "};
        model.addRow(rowData2);
        Object[] rowData3 =  {"1 ", "50.10 ", "26.35 ", "1.28 ", "220 ", "8.10 ", "1.81 "};
        model.addRow(rowData3);
        Object[] rowData4 = {"1 ", "50.20 ", "24.53", "1.59 ", "220 ", "8.40 ", "1.87 "};
        model.addRow(rowData4);
        Object[] rowData5 =  {"1 ", "50.10 ", "22.68 ", "1.79 ", "220 ", "8.50 ", "1.91 "};
        model.addRow(rowData5);
        Object[] rowData6 = {"1 ", "50.20 ", "18.98 ", "2.15 ", "220 ", "8.76 ", "1.95 "};
        model.addRow(rowData6);
        Object[] rowData7 =  {"1 ", "50.00 ", "9.53 ", "2.69 ", "220 ", "8.98 ", "1.99 "};
        model.addRow(rowData7);
        //Object[] rowData8 =  {"", " ", " ", " ", " ", " ", " "};
        //      model.addRow(rowData8);






        //0  for (int i = 0; i < 10; i++) {
        // model.addRow(rowData);
        //      table.setValueAt(i + 1, i, 0);
        // }*/
        Object[] rowData9 = {"Multiplication Factor ", "1 ", "1 ", "1 ", "1 ", "1 ", "1"};
        model.addRow(rowData9);
          for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new RPStatusColumnCellRenderer());
        }
        JScrollPane tScroller = new JScrollPane(table);
        tScroller.setBounds(20, 326, 984, 310);
        add(tScroller);
        setVisible(true);
    }

    public void useSavedValues(EntryValues values) {
        this.slNoField.setText(values.getSlNoFieldString());
        ipNoField.setText(values.getIpNoFieldString());
        pumpTypeField.setText(values.getPumpTypeFieldString());
        ratingField.setText(values.getRatingFieldString());
        headField.setText(values.getHeadFieldString());
        dischField.setText(values.getDischFieldString());
        effField.setText(values.getEffFieldString());
        currField.setText(values.getCurrFieldString());
        voltField.setText(values.getVoltFieldString());
        phaseField.setText(values.getPhaseFieldString());
        freqField.setText(values.getFreqFieldString());
        hRangeLwrField.setText(values.gethRangeLwrFieldString());
        hRangeUprField.setText(values.gethRangeUprFieldString());
        delSizeField.setText(values.getDelSizeFieldString());
        gaugDistField.setText(values.getGaugDistFieldString());
        isRefField.setText(values.getIsRefFieldString());
        remarksField.setText(values.getRemarksFieldString());
        dateChooser.setDate(values.getDateFieldDate());
        int vRows = values.getTableValueStrings().length;
        int vCols = 0;//default
        if (vRows != 0) {
            vCols = values.getTableValueStrings()[0].length;
        }
        String[][] tableValues = values.getTableValueStrings();
        for (int i = 0; i < vRows; i++) {
            if (i >= table.getRowCount()) {
                break;
            }
            for (int j = 0; j < vCols; j++) {

                table.setValueAt(tableValues[i][j], i, j);
            }
        }



    }

    public void useSavedTypeValues(TypeValues values) {

        pumpTypeField.setText(values.getPumpTypeFieldString());
        ratingField.setText(values.getRatingFieldString());
        headField.setText(values.getHeadFieldString());
        dischField.setText(values.getDischFieldString());
        effField.setText(values.getEffFieldString());
        currField.setText(values.getCurrFieldString());
        voltField.setText(values.getVoltFieldString());
        phaseField.setText(values.getPhaseFieldString());
        freqField.setText(values.getFreqFieldString());
        hRangeLwrField.setText(values.gethRangeLwrFieldString());
        hRangeUprField.setText(values.gethRangeUprFieldString());
        delSizeField.setText(values.getDelSizeFieldString());
        isRefField.setText(values.getIsRefFieldString());

    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        ReadingEntryPanel rPanel = new ReadingEntryPanel();
        f.setSize(768, 100);
        f.setLocation(100, 100);
        f.getContentPane().add(rPanel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    /**
     * @return the slNoField
     */
    public JTextField getSlNoField() {
        return slNoField;
    }

    /**
     * @return the ipNoField
     */
    public JTextField getIpNoField() {
        return ipNoField;
    }

    /**
     * @return the pumpTypeField
     */
    public JTextField getPumpTypeField() {
        String type = pumpTypeField.getText();
        pumpTypeField.setText(type.replace(" ",""));
        return pumpTypeField;
    }

    /**
     * @return the ratingField
     */
    public JTextField getRatingField() {
        return ratingField;
    }

    /**
     * @return the headField
     */
    public JTextField getHeadField() {
        return headField;
    }

    /**
     * @return the dischField
     */
    public JTextField getDischField() {
        return dischField;
    }

    /**
     * @return the effField
     */
    public JTextField getEffField() {
        return effField;
    }

    /**
     * @return the currField
     */
    public JTextField getCurrField() {
        return currField;
    }

    /**
     * @return the voltField
     */
    public JTextField getVoltField() {
        return voltField;
    }

    /**
     * @return the phaseField
     */
    public JTextField getPhaseField() {
        return phaseField;
    }

    /**
     * @return the freqField
     */
    public JTextField getFreqField() {
        return freqField;
    }

    /**
     * @return the hRangeLwrField
     */
    public JTextField gethRangeLwrField() {
        return hRangeLwrField;
    }

    /**
     * @return the hRangeUprField
     */
    public JTextField gethRangeUprField() {
        return hRangeUprField;
    }

    /**
     * @return the delSizeField
     */
    public JTextField getDelSizeField() {
        return delSizeField;
    }

    /**
     * @return the gaugDistField
     */
    public JTextField getGaugDistField() {
        return gaugDistField;
    }

    /**
     * @return the isRefField
     */
    public JTextField getIsRefField() {
        return isRefField;
    }

    /**
     * @return the remarksField
     */
    public JTextField getRemarksField() {
        return remarksField;
    }
    // public JTextField getDatestringField() {
    //     return dateStringField;
    // }

    /**
     * @return the table
     */
    public JTable getTable() {
        return table;
    }

    /**
     * @return the dateChooser
     */
    public JDateChooser getDateChooser() {
        return dateChooser;
    }

    /**
     * @return the dischMaxForScaleField
     */
    public JTextField getDischMaxForScaleField() {
        return dischMaxForScaleField;
    }

    /**
     * @return the headMaxForScaleField
     */
    public JTextField getHeadMaxForScaleField() {
        return headMaxForScaleField;
    }

    /**
     * @return the effMaxForScaleField
     */
    public JTextField getEffMaxForScaleField() {
        return effMaxForScaleField;
    }

    /**
     * @return the currMaxForScaleField
     */
    public JTextField getCurrMaxForScaleField() {
        return currMaxForScaleField;
    }

    /**
     * @return the gridLinesType
     */
    public GridLinesType getGridLinesType() {
        return gridLinesType;
    }

    /**
     * @param gridLinesType the gridLinesType to set
     */
    public void setGridLinesType(GridLinesType gridLinesType) {
        this.gridLinesType = gridLinesType;
    }

    /**
     * @return the desiredSmoothnessPercentage
     */
    public double getDesiredSmoothnessPercentage() {
        return desiredSmoothnessPercentage;
    }

    /**
     * @param desiredSmoothnessPercentage the desiredSmoothnessPercentage to set
     */
    public void setDesiredSmoothnessPercentage(double desiredSmoothnessPercentage) {
        this.desiredSmoothnessPercentage = desiredSmoothnessPercentage;
    }
class NumericVerifier extends InputVerifier {
         public boolean verify(JComponent input) {
             JTextField tf = (JTextField) input;
            try{
                double ok =Double.parseDouble(tf.getText());
               
                return true;
            } 
            catch(Exception ex){}
            JOptionPane.showMessageDialog(ReadingEntryPanel.this,"Here , you can enter numbers only!");
            return false;
         }
     }

}
class RPStatusColumnCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        //Cells are by default rendered as a JLabel.
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        //Get the status for the current row.
        //CustomTableModel tableModel = (CustomTableModel) table.getModel();
        // if (tableModel.getStatus(row) == CustomTableModel.APPROVED) {
        if (isSelected && hasFocus) {
            l.setBackground(Color.GREEN);

        }
        else{
            l.setBackground(Color.CYAN); 
         }
        //} else {
        // l.setBackground(Color.RED);
        // }

        //Return the JLabel which renders the cell.
        return l;

    }


    


}