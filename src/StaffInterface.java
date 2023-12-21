public interface StaffInterface {
// adaugarea unei productii in sistem
    public void addProductionSystem(Production p);
// adăugarea unui actor în sistem
    public void addActorSystem(Actor a);
// stergerea unei productii care a fost adaugata de el din sistem
    public void removeProductionSystem(String name);
// stergerea unui actor care a fost adaugat de el din sistem
    public void removeActorSystem(String name);
// actualizarea detaliilor despre o productie
// care a fost adaugată de el in sistem
    public void updateProduction(Production p);
//  actualizarea detaliilor despre un actor care a fost adaugat de el in sistem
    public void updateActor(Actor a);
//    rezolvarea cererilor primite de utilizatori
    // TODO : I guess

}
