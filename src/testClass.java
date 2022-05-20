public class testClass {

    public static void main(String[] args) {

//        BTree bT = new BTree(3);
//
//        bT.insert(9,9);
//        bT.insert(15, 15);
//        bT.insert(17, 17);
//        bT.insert(23, 23);
//        bT.insert(67, 67);
//        bT.insert(89, 89);
//        bT.insert(34, 34);
//        bT.insert(57, 57);
//        bT.insert(199, 199);
//        bT.insert(3, 3);
//        bT.insert(4, 4);

        BTree bT = new BTree(4);

        bT.insert(34, 34);
        bT.insert(3, 3);
        bT.insert(46, 46);
        bT.insert(74, 74);
        bT.insert(56, 56);
        bT.insert(8, 8);
        bT.insert(97, 97);
        bT.insert(64, 64);
        bT.insert(14, 14);
        bT.insert(25, 25);
        bT.insert(100, 100);
        bT.insert(120, 120);
        bT.insert(15, 15);
        bT.insert(20, 20);
        bT.insert(34, 35);

        bT.recursivePrintTree(bT.root, 0);

//        bT.search(89, 89);

    }

}
