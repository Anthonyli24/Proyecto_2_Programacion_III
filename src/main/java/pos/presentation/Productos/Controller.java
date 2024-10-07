package pos.presentation.Productos;

import pos.logic.Categoria;
import pos.logic.Producto;
import pos.logic.Service;
import pos.Application;
import java.util.List;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.init();
        this.view = view;
        this.model = model;
        model.setCategorias(Service.instance().search(new Categoria()));
        model.setList(Service.instance().search(model.getFilter()));
        view.setController(this);
        view.setModel(model);
    }

    public void search(Producto filter) throws  Exception{
        model.setFilter(filter);
        List<Producto> rows = Service.instance().search(model.getFilter());
        model.setMode(Application.MODE_CREATE);
        model.setList(rows);
    }

    public void save(Producto e) throws  Exception{
        switch (model.getMode()) {
            case Application.MODE_CREATE:
                Service.instance().create(e);
                break;
            case Application.MODE_EDIT:
                Service.instance().update(e);
                break;
        }
        model.setFilter(new Producto());
        search(model.getFilter());
    }

    public void edit(int row){
        Producto e = model.getList().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {}
    }

    public void delete() throws Exception {
        Service.instance().delete(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Producto());
    }
}