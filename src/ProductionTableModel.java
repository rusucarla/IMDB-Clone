import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductionTableModel extends AbstractTableModel {
    private final List<Production> productions; // Lista de producții
    private final String[] columnNames = new String[]{"Titlu", "Genuri", "Rating"}; // Numele coloanelor

    public ProductionTableModel(List<Production> productions) {
        this.productions = productions;
    }
    public Production getProductionAt(int rowIndex) {
        return productions.get(rowIndex);
    }
    @Override
    public int getRowCount() {
        return productions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Production production = productions.get(rowIndex);
        switch (columnIndex) {
            case 0: return production.getTitlu();
            case 1: return production.getGenreList();
            case 2: return production.getNotaFilm();
            default: return null;
        }
    }

    // Metodă pentru actualizarea datelor
    public void setProductions(List<Production> productions) {
        this.productions.clear();
        this.productions.addAll(productions);
        fireTableDataChanged(); // Notifică tabelul că datele s-au schimbat
    }
}
