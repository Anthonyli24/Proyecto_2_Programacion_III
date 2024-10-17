package pos.presentation.Estadistica;

import java.util.ArrayList;
import pos.logic.Categoria;
import pos.logic.Service;
import pos.logic.Rango;
import java.util.List;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(View view, Model model) {
        List<Categoria> catTable = new ArrayList<>();
        model.Init(catTable);
        model.setCategoriasCombobox(Service.instance().search(new Categoria()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void ActualizarData() throws Exception {
        Rango r = model.getRango();
        List<Categoria> categorias = model.getCategorias();

        int colCount = ((r.getAnnoHasta() - r.getAnnoDesde()) * 12 + r.getMesHasta() - r.getMesDesde()) + 1;
        int rowCount = categorias.size();

        String[] cols = new String[colCount];
        String[] rows = new String[rowCount];

        int mes = r.getMesDesde();
        int anno = r.getAnnoDesde();
        for (int i = 0; i < colCount; i++) {
            cols[i] = "Mes " + mes + " - Año " + anno;
            mes++;
            if (mes > 12) {
                mes = 1;
                anno++;
            }
        }
        for (int i = 0; i < rowCount; i++) {
            rows[i] = categorias.get(i).getNombre();
        }
        float[][] data = new float[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            Categoria categoria = categorias.get(i);
            for (int j = 0; j < colCount; j++) {
                int mesActual = r.getMesDesde() + j;
                int annoActual = r.getAnnoDesde();
                if (mesActual > 12) {
                    mesActual -= 12;
                    annoActual++;
                }
                String fechaInicio = annoActual + "-" + (mesActual < 10 ? "0" + mesActual : mesActual);
                String fechaFin = annoActual + "-" + (mesActual < 10 ? "0" + mesActual : mesActual);
                data[i][j] = (float) Service.instance().Estadistica(fechaInicio, fechaFin, categoria.getId());
            }
        }
        model.setData(rows, cols, data);
    }
}