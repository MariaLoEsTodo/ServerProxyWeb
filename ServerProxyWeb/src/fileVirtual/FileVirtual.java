/**************************************************************************
*   Comunicaciones y Redes Proyecto: Proxy Web María
*   Members:
*       - Juan Sebastián Barreto Jimenéz
*       - Janet Chen He
*       - María José Niño Rodriguez
*       - David Santiago Quintana Echavarria
*   File: 
*       FileVirtual.java
*   Purpose: 
*       Class for manage Virtual Web Sites 
**************************************************************************/
/* Package */
package fileVirtual;

public class FileVirtual {
    private String nombreHostVirtual;
    private String hostReal;
    private String directorio;
    
    public FileVirtual(String nombreHostVirtual, String hostReal, String directorio) {
        this.nombreHostVirtual = nombreHostVirtual;
        this.hostReal = hostReal;
        this.directorio = directorio;
    } // end builder FileVirtual

    public FileVirtual() {
    } // end builder FileVirtual

    public String getNombreHostVirtual() {
        return nombreHostVirtual;
    } // end getNombreHostVirtual

    public void setNombreHostVirtual(String nombreHostVirtual) {
        this.nombreHostVirtual = nombreHostVirtual;
    } // end setNombreHostVirtual

    public String getHostReal() {
        return hostReal;
    } // end getHostReal

    public void setHostReal(String hostReal) {
        this.hostReal = hostReal;
    } // end setHostReal

    public String getDirectorio() {
        return directorio;
    } // end getDirectorio

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    } // end setDirectorio
    
} // end class FileVirtual
