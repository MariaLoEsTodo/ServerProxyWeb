/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileVirtual;

/**
 *
 * @author david
 */
public class FileVirtual {
    private String nombreHostVirtual;
    private String hostReal;
    private String directorio;
    
    
    
    
    
    public FileVirtual(String nombreHostVirtual, String hostReal, String directorio) {
        this.nombreHostVirtual = nombreHostVirtual;
        this.hostReal = hostReal;
        this.directorio = directorio;
    }


    public FileVirtual() {
    }

    public String getNombreHostVirtual() {
        return nombreHostVirtual;
    }

    public void setNombreHostVirtual(String nombreHostVirtual) {
        this.nombreHostVirtual = nombreHostVirtual;
    }

    public String getHostReal() {
        return hostReal;
    }

    public void setHostReal(String hostReal) {
        this.hostReal = hostReal;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    
}
