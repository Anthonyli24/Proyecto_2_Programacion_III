package pos;

import pos.logic.Service;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.windows.WindowLookAndFeel");
        }
        catch (Exception ex) {}

        window = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();
        window.setContentPane(tabbedPane);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Service.instance().stop();
            }
        });

        pos.presentation.Clientes.Model clientesModel= new pos.presentation.Clientes.Model();
        pos.presentation.Clientes.View clientesView = new pos.presentation.Clientes.View();
        clientesController = new pos.presentation.Clientes.Controller(clientesView,clientesModel);
        Icon clientesIcon= new ImageIcon(Application.class.getResource("/pos/presentation/icons/cliente.png"));

        pos.presentation.Cajero.Model cajeroModel = new pos.presentation.Cajero.Model();
        pos.presentation.Cajero.View cajeroView = new pos.presentation.Cajero.View();
        cajeroController = new pos.presentation.Cajero.Controller(cajeroView,cajeroModel);
        Icon cajeroIcon = new ImageIcon(Application.class.getResource("/pos/presentation/icons/cajeros.png"));

        pos.presentation.Productos.Model productosModel= new pos.presentation.Productos.Model();
        pos.presentation.Productos.View productosView = new pos.presentation.Productos.View();
        productosController = new pos.presentation.Productos.Controller(productosView,productosModel);
        Icon productosIcon= new ImageIcon(Application.class.getResource("/pos/presentation/icons/producto.png"));

        tabbedPane.addTab("Clientes  ",clientesIcon, clientesView.getPanel());
        tabbedPane.addTab("Cajeros  ",cajeroIcon, cajeroView.getPanel());
        tabbedPane.addTab("Productos  ",productosIcon,productosView.getPanel());

        window.setSize(900,550);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setIconImage((new ImageIcon(Application.class.getResource("presentation/icons/icon.png"))).getImage());
        window.setTitle("POS: Point Of Sale");
        window.setVisible(true);
    }

    public static pos.presentation.Clientes.Controller clientesController;
    public static pos.presentation.Cajero.Controller cajeroController;
    public static pos.presentation.Productos.Controller productosController;

    public static JFrame window;

    public final static int MODE_CREATE=1;
    public final static int MODE_EDIT=2;

    public static Border BORDER_ERROR = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED);
}
