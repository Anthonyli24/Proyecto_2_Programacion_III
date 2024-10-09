package pos.presentation.Productos;

import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
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

    public void search(Producto filter) throws Exception {
        model.setFilter(filter);
        List<Producto> rows = Service.instance().search(model.getFilter());
        model.setMode(Application.MODE_CREATE);
        model.setList(rows);
    }

    public void save(Producto e) throws Exception {
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

    public void edit(int row) {
        Producto e = model.getList().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {
        }
    }

    public void delete() throws Exception {
        Service.instance().delete(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Producto());
    }

    public void print() throws Exception {
        String dest = "productos.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        try (PdfWriter writer = new PdfWriter(dest);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.setMargins(20, 20, 20, 20);

            Table header = new Table(1);
            header.setWidth(550);
            header.setHorizontalAlignment(HorizontalAlignment.CENTER);
            header.addCell(getCell(new Paragraph("Listado de Productos")
                    .setFont(font)
                    .setBold()
                    .setFontSize(22f), TextAlignment.CENTER, false));
            document.add(header);

            document.add(new Paragraph(""));
            document.add(new Paragraph(""));

            Color headerBkg = ColorConstants.DARK_GRAY;
            Color headerFrg = ColorConstants.WHITE;
            Color rowBkg1 = ColorConstants.LIGHT_GRAY;
            Color rowBkg2 = ColorConstants.WHITE;

            Table body = new Table(new float[]{3, 3, 3, 3, 3, 3});
            body.setWidth(550);
            body.setHorizontalAlignment(HorizontalAlignment.CENTER);

            body.addCell(getStyledHeaderCell("Código", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Descripción", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Unidad", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Precio Unitario", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Existencia", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Categoría", headerBkg, headerFrg));

            boolean isOddRow = true;

            for (Producto e : model.getList()) {
                Color rowBkg = isOddRow ? rowBkg1 : rowBkg2;
                body.addCell(getStyledBodyCell(e.getCodigo(), rowBkg));
                body.addCell(getStyledBodyCell(e.getDescripcion(), rowBkg));
                body.addCell(getStyledBodyCell(e.getUnidadMedida(), rowBkg));
                body.addCell(getStyledBodyCell(String.valueOf(e.getPrecioUnitario()), rowBkg));
                body.addCell(getStyledBodyCell(String.valueOf(e.getExistencias()), rowBkg));
                body.addCell(getStyledBodyCell(e.getCategoria().getNombre(), rowBkg));
                isOddRow = !isOddRow;
            }
            document.add(body);
        }
    }

    private Cell getStyledHeaderCell(String content, Color bkgColor, Color frgColor) {
        return getCell(new Paragraph(content)
                .setBold()
                .setFontSize(12f)
                .setBackgroundColor(bkgColor)
                .setFontColor(frgColor)
                .setPadding(5), TextAlignment.CENTER, true);
    }

    private Cell getStyledBodyCell(String content, Color bkgColor) {
        return getCell(new Paragraph(content)
                .setFontSize(10f)
                .setPadding(5)
                .setBackgroundColor(bkgColor), TextAlignment.CENTER, true);
    }

    private Cell getCell(Paragraph content, TextAlignment alignment, boolean border) {
        Cell cell = new Cell().add(content);
        cell.setTextAlignment(alignment);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);  // Centrado vertical
        if (!border) {
            cell.setBorder(Border.NO_BORDER);
        }
        return cell;
    }
}