package pos.logic;

public class Rango {
    int annoDesde;
    int mesDesde;
    int annoHasta;
    int mesHasta;

    public Rango(int annoDesde, int mesDesde, int annoHasta, int mesHasta){
        this.annoDesde = annoDesde;
        this.mesDesde = mesDesde;
        this.annoHasta = annoHasta;
        this.mesHasta = mesHasta;
    }

    public int getAnnoDesde() {
        return annoDesde;
    }
    public int getMesDesde() {
        return mesDesde;
    }
    public int getAnnoHasta() {
        return annoHasta;
    }
    public int getMesHasta(){return mesHasta;}

    public void setAnnoDesde(int annoDesde) {
        this.annoDesde = annoDesde;
    }
    public void setMesDesde(int mesDesde) {
        this.mesDesde = mesDesde;
    }
    public void setAnnoHasta(int annoHasta) {
        this.annoHasta = annoHasta;
    }
    public void setMesHasta(int mesHasta){
        this.mesHasta = mesHasta;
    }
}