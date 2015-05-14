/*
 * Copyright (C) 2013 DobinRutishauser@broken.ch
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gui.categorizer;

import gui.categorizer.model.Category;
import gui.categorizer.model.CategoryEntry;
import java.awt.Color;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dobin
 */
public class CategorizerTableModel extends AbstractTableModel {
    private Category category;
        
    public CategorizerTableModel() {
        // Fake init
        category = new Category();
    }
    
    
    public void loadCategory(Category category) {
        this.category = category;
        this.fireTableDataChanged();
    }
    
    
    @Override
    public int getRowCount() {
        return category.getCategoryEntries().size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return Boolean.class;
            case 1:
                return Color.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            default:
                return String.class;
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return category.getCategoryEntries().get(rowIndex).isEnabled();
            case 1:
                return category.getCategoryEntries().get(rowIndex).getColor();
            case 2:
                return category.getCategoryEntries().get(rowIndex).getTag();
            case 3:
                return category.getCategoryEntries().get(rowIndex).getRegex();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: 
                return "Active";
            case 1:
                return "Color";
            case 2:
                return "Tag";
            case 3:
                return "String";
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                category.getCategoryEntries().get(rowIndex).setEnabled( (Boolean) aValue);
                break;
            case 1:
                category.getCategoryEntries().get(rowIndex).setColor( (Color) aValue);
                break;
            case 2:
                category.getCategoryEntries().get(rowIndex).setTag((String) aValue);
                break;
            case 3:
                category.getCategoryEntries().get(rowIndex).setRegex((String) aValue);
                break;
            default:
                break;
        }
        
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    

    void addEmptyLine() {
        char c = (char) (65 + getRowCount());
        String res = Character.toString(c) +Character.toString(c);
        
        category.getCategoryEntries().add( new CategoryEntry(res, "XXX"));
        this.fireTableDataChanged();
    }

    Category getCurrentCategory() {
        return category;
    }
    
    void removeRow(int selectedRow) {
        category.getCategoryEntries().remove(selectedRow);
        this.fireTableDataChanged();
    }

}
