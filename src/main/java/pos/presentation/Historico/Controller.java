package pos.presentation.Historico;

import com.itextpdf.layout.properties.VerticalAlignment;
import pos.Application;
import pos.logic.Factura;
import pos.logic.Service;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.util.List;

public class Controller {
    view View;
    Model model;

    public Controller(view view, Model model) throws Exception {
        model.init(Service.instance().search(new Factura()));
        this.View = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void search(Factura filter) throws Exception {
        model.setFilter(filter);
        List<Factura> rows = Service.instance().searchCliente(model.getFilter());
        model.setMode(Application.MODE_CREATE);
        model.setList(rows);
    }

    public void buscarFactura(String filter) throws Exception {
        Factura fact = Service.instance().read(filter);
        if (fact != null) {
            model.setListalineas(Service.instance().searchHistorico(fact.getCodigoFactura()));
        }
    }

    public void save(Factura e) throws Exception {
        switch (model.getMode()) {
            case Application.MODE_CREATE:
                Service.instance().create(e);
                break;
            case Application.MODE_EDIT:
                Service.instance().update(e);
                break;
        }
        model.setFilter(new Factura());
        search(model.getFilter());
    }

    public void edit(int row) {
        Factura e = model.getList().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().read(e.getCodigoFactura()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete() throws Exception {
        Service.instance().delete(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Factura());
    }

    public List<Factura> loadFacturas() throws Exception {
        List<Factura> facturas = Service.instance().search(new Factura());
        model.setList(facturas);
        return facturas;
    }

    public void print() throws Exception {
        String dest = "facturas.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        try (PdfWriter writer = new PdfWriter(dest);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.setMargins(20, 20, 20, 20);

            Table header = new Table(1);
            header.setWidth(550);
            header.setHorizontalAlignment(HorizontalAlignment.CENTER);
            header.addCell(getCell(new Paragraph("Listado de Facturas")
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

            Table body = new Table(new float[]{2, 4, 4, 3, 3});
            body.setWidth(550);
            body.setHorizontalAlignment(HorizontalAlignment.CENTER);

            body.addCell(getStyledHeaderCell("Código", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Fecha", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Nombre-Cliente", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Nombre-Cajero", headerBkg, headerFrg));
            body.addCell(getStyledHeaderCell("Total", headerBkg, headerFrg));

            boolean isOddRow = true;

            for (Factura e : model.getList()) {
                Color rowBkg = isOddRow ? rowBkg1 : rowBkg2;
                body.addCell(getStyledBodyCell(e.getCodigoFactura(), rowBkg));
                body.addCell(getStyledBodyCell(e.getFecha(), rowBkg));
                body.addCell(getStyledBodyCell(e.getNombreCli(), rowBkg));
                body.addCell(getStyledBodyCell(e.getNombreCaje(), rowBkg));
                body.addCell(getStyledBodyCell(String.valueOf(e.getImporteTotal()), rowBkg));
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
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        if (!border) {
            cell.setBorder(Border.NO_BORDER);
        }
        return cell;
    }

}
