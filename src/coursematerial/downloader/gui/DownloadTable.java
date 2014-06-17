package coursematerial.downloader.gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class DownloadTable extends AbstractTableModel {
	private final String columnNames[] = new String[2];
	private ArrayList<Object[]> rows = new ArrayList<Object[]>();
	
	public DownloadTable(String downloadType) {
		columnNames[0] = downloadType;
		columnNames[1] = "Download?";
	}
	
	public void addRows(String text) {
		Object[] newRow = new Object[2];
		newRow[0] = text;
		newRow[1] = false;
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
        if (columnIndex == 1 && aValue instanceof Boolean) {
            rows.get(rowIndex)[columnIndex] = aValue;
        }
    }
	
	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	public Object getTitle(int row) {
		return rows.get(row)[0];
	}
	
	public Object isSelected(int row) {
		return rows.get(row)[1];
	}
}
