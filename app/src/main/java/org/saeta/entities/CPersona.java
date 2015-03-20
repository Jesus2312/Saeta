package org.saeta.entities;

/**
 * Created by jlopez on 3/19/2015.
 */
public class CPersona {


    @Override
    public  String toString ()
    {
        return this.Paterno +" "+ this.Materno +" "+ this.Nombre;
    }
    public void setCalle(String calle) {
        Calle = calle;
    }

    public void setCodigoPostal(String codigoPostal) {
        CodigoPostal = codigoPostal;
    }

    public void setColonia(String colonia) {
        Colonia = colonia;
    }

    public void setDistritoFederal(int distritoFederal) {
        DistritoFederal = distritoFederal;
    }

    public void setDistritoLocal(int distritoLocal) {
        DistritoLocal = distritoLocal;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public void setIdDetectado(int idDetectado) {
        IdDetectado = idDetectado;
    }

    public void setLatitud(float latitud) {
        Latitud = latitud;
    }

    public void setLongitud(float longitud) {
        Longitud = longitud;
    }

    public void setManzana(int manzana) {
        Manzana = manzana;
    }

    public void setMaterno(String materno) {
        Materno = materno;
    }

    public void setMunicipio(String municipio) {
        Municipio = municipio;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setNumExterior(String numExterior) {
        NumExterior = numExterior;
    }

    public void setNumInterior(String numInterior) {
        NumInterior = numInterior;
    }

    public void setPaterno(String paterno) {
        Paterno = paterno;
    }

    public void setSeccion(int seccion) {
        Seccion = seccion;
    }

    public void setTelefono1(String telefono1) {
        Telefono1 = telefono1;
    }

    public void setTelefono2(String telefono2) {
        Telefono2 = telefono2;
    }

    public void setTelefono3(String telefono3) {
        Telefono3 = telefono3;
    }

    private String Calle;
    private String CodigoPostal ;
    private String Colonia;
    private  int DistritoFederal ;
    private  int DistritoLocal;
    private String  Estado;
    private int IdDetectado;
    private float Latitud;
    private float Longitud;
    private  int Manzana;
    private  String Materno ;
    private String Municipio;
    private String Nombre;
    private String NumExterior;
    private String NumInterior;
    private String Paterno;
    private int Seccion ;
    private String Telefono1;
    private String Telefono2;
    private String Telefono3;

    public int getEncuestaId() {
        return EncuestaId;
    }

    public void setEncuestaId(int encuestaId) {
        EncuestaId = encuestaId;
    }

    private int EncuestaId;

    public CPersona ()
    {

    }

    public String getCalle() {
        return Calle;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public String getColonia() {
        return Colonia;
    }

    public int getDistritoFederal() {
        return DistritoFederal;
    }

    public int getDistritoLocal() {
        return DistritoLocal;
    }

    public String getEstado() {
        return Estado;
    }

    public int getIdDetectado() {
        return IdDetectado;
    }

    public float getLatitud() {
        return Latitud;
    }

    public float getLongitud() {
        return Longitud;
    }

    public int getManzana() {
        return Manzana;
    }

    public String getMaterno() {
        return Materno;
    }

    public String getMunicipio() {
        return Municipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getNumExterior() {
        return NumExterior;
    }

    public String getNumInterior() {
        return NumInterior;
    }

    public String getPaterno() {
        return Paterno;
    }

    public int getSeccion() {
        return Seccion;
    }

    public String getTelefono1() {
        return Telefono1;
    }

    public String getTelefono2() {
        return Telefono2;
    }

    public String getTelefono3() {
        return Telefono3;
    }
}
