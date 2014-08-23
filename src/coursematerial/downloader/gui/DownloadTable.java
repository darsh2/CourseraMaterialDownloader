package coursematerial.downloader.gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class DownloadTable extends AbstractTableModel {
	private final String columnNames[] = new String[6];
	private ArrayList<Object[]> rows = new ArrayList<Object[]>();
	
	public DownloadTable(String downloadType) {
		columnNames[0] = downloadType;
		columnNames[1] = "Lectures"; columnNames[2] = "Subtitles(txt)"; columnNames[3] = "Subtitles(srt)";
		columnNames[4] = "Slides(pdf)"; columnNames[5] = "Slides(ppt)";
	}
	
	public void addRows(String text) {
		Object[] newRow = new Object[6];
		newRow[0] = text;
		for (int i = 1; i < 6; i++)
			newRow[i] = false;
		rows.add(newRow);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		return rows.get(row)[column];
	}
	
	@Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? String.class : Boolean.class;
    }
	
	@Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex > 0 && aValue instanceof Boolean) {
            rows.get(rowIndex)[columnIndex] = aValue;
        }
    }
	
	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	public Object getTitle(int row) {
		return rows.get(row)[0];
	}
	
	public ArrayList<Object> isSelected(int row) {
		ArrayList<Object> selections = new ArrayList<>();
		for (int i = 1; i < 6; i++)
			selections.add(rows.get(row)[i]);
		return selections;
	}
}
