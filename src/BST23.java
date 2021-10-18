public class BST23<T extends  Comparable<T>> {
    private BST23Node<T> _root;

    public BST23(){
        _root = null;
    }

    public boolean delete(BST23Node pNode){
        BST23Node deletedNode = find(pNode);
        if (deletedNode != null){
            if (deletedNode == _root && isLeaf(deletedNode) && !deletedNode.isThreeNode()){
                //jediny prvok v strome tak sa zmaze referencia na root
                _root = null;
                return true;
            }
            if (deletedNode.isThreeNode() && isLeaf(deletedNode)){
                if (pNode.get_data1().compareTo(deletedNode.get_data1()) == 0){
                    deletedNode.set_data1(deletedNode.get_data2());
                    deletedNode.set_data2(null);
                    deletedNode.set_isThreeNode(false);
                    return true;
                }else if (pNode.get_data1().compareTo(deletedNode.get_data2()) == 0){
                    deletedNode.set_data2(null);
                    deletedNode.set_isThreeNode(false);
                    return true;
                }
            }
            BST23Node alterNode = findInOrderNode(deletedNode, pNode);
            if (alterNode != null && alterNode.isThreeNode()){
                //jeho nasledovnik je 3 vrchol
                if (pNode.get_data1().compareTo(deletedNode.get_data1()) == 0){
                    deletedNode.set_data1(alterNode.get_data1());
                    alterNode.set_data1(alterNode.get_data2());
                    alterNode.set_data2(null);
                    alterNode.set_isThreeNode(false);
                    return true;
                }else if (pNode.get_data1().compareTo(deletedNode.get_data2()) == 0){
                    deletedNode.set_data2(alterNode.get_data1());
                    alterNode.set_data1(alterNode.get_data2());
                    alterNode.set_data2(null);
                    alterNode.set_isThreeNode(false);
                    return true;
                }
            }
            //vymazanie prvku a tym vytvorenie prazdneho miesta v alter node
            if (pNode.get_data1().compareTo(deletedNode.get_data1()) == 0){
                deletedNode.set_data1(alterNode.get_data1());
            }else if (pNode.get_data1().compareTo(deletedNode.get_data2()) == 0){
                deletedNode.set_data2(alterNode.get_data1());
                return true;
            }
            while (alterNode != null){
                BST23Node brotherNode = findBrother(alterNode);
                if (brotherNode.isThreeNode()){
                    //TODO pokracovat

                }
            }
        }
        return false;
    }

    public BST23Node findBrother(BST23Node node){
        if (node.get_parent().get_left1() == node){
            //node pre ktoreho hladam brata je uplne lavym synom jeho otca
            if (node.get_parent().get_right1() != null){
                return node.get_parent().get_right1();
            }
        }else if(node.get_parent().get_right1() == node){
            //node pre ktoreho hladam brata je pravym synom pre prvy prvok v node jeho otca
            if (node.get_parent().get_right2() == null){
                //otec ma len dvoch synov
                return node.get_parent().get_left1();
            }else {
                //otec ma troch synov cize mam na vyber dvoch bratov
                //uprednostnujem 3 vrchol
                if (node.get_parent().get_left1().isThreeNode()){
                    return node.get_parent().get_left1();
                }else{
                    return node.get_parent().get_right2();
                }
            }
        }else if(node.get_parent().get_right2() != null && node.get_parent().get_right2() == node){
            //node pre ktoreho hladam brata je pravym synom pre druhy prvok v node jeho otca
            if (node.get_parent().get_right2() != null){
                return node.get_parent().get_right2();
            }
        }
        return null;
    }

    public BST23Node findInOrderNode(BST23Node node, BST23Node nodeData){
        if (isLeaf(node)){
            return node;
        }
        if (nodeData.get_data1().compareTo(node.get_data1()) == 0){
            //data ktore mazem su nalavo
            BST23Node temp = node.get_right1();
            while (!isLeaf(temp)){
                temp.get_left1();
            }
            return temp;
        }else if(nodeData.get_data1().compareTo(node.get_data2()) == 0){
            //data ktore mazem su napravo
            BST23Node temp = node.get_right2();
            while (!isLeaf(temp)){
                temp.get_left1();
            }
            return temp;
        }
        return null;
    }

    public boolean isLeaf(BST23Node node){
        if (node.get_left1() == null && node.get_right1() == null && node.get_right2() == null){
            return true;
        }
        return false;
    }

    public boolean insert(BST23Node pNode){
        if (_root == null){
            _root = pNode;
            return true;
        }else if(find(pNode) == null) {
            BST23Node leaf = findLeafForInsert(pNode);
            if (leaf != null){
                if (!leaf.isThreeNode()){
                    //vkladanie pokial ma list len jeden kluc(data1)
                    leaf.set_isThreeNode(true);
                    if (leaf.get_data1().compareTo(pNode.get_data1()) > 0){
                        leaf.set_data2(pNode.get_data1());
                        return true;
                    }else if(leaf.get_data1().compareTo(pNode.get_data1()) < 0){
                        leaf.set_data2(leaf.get_data1());
                        leaf.set_data1(pNode.get_data1());
                        return true;
                    }
                }else {
                    //list je trojvrchol
                    while (leaf != null){
                        T min = getMin(leaf, pNode);
                        T max = getMax(leaf, pNode);
                        T middle = getMiddle(leaf, pNode);
                        if (leaf == _root){
                            //ked je node korenom
                            BST23Node newRoot = new BST23Node(middle);
                            BST23Node newRightSon = new BST23Node(max);

                            newRoot.set_left1(leaf);
                            newRoot.set_right1(newRightSon);

                            newRightSon.set_left1(leaf.get_left2());
                            newRightSon.set_right1(leaf.get_right2());
                            newRightSon.set_parent(newRoot);

                            leaf.set_parent(newRoot);
                            leaf.set_isThreeNode(false);
                            leaf.set_data2(null);
                            leaf.set_data1(min);
                            if (leaf.get_right2() != null && leaf.get_left2() != null){
                                leaf.get_right2().set_parent(newRightSon);
                                leaf.get_left2().set_parent(newRightSon);
                            }
                            leaf.set_left2(null);
                            leaf.set_right2(null);

                            _root = newRoot;
                            return true;
                        }else {
                            //node nie je korenom
                            if (!leaf.get_parent().isThreeNode()){
                                //pokial je otec dvojvrchol
                                if (leaf.get_parent().get_left1() == leaf){
                                    //ak je lavy potomok otca
                                    BST23Node newNode = new BST23Node(max);
                                    newNode.set_parent(leaf.get_parent());
                                    newNode.set_left1(leaf.get_left2());
                                    newNode.set_right1(leaf.get_right2());
                                    if (leaf.get_left2() != null && leaf.get_right2() != null){
                                        leaf.get_left2().set_parent(newNode);
                                        leaf.get_right2().set_parent(newNode);
                                    }
                                    leaf.set_left2(null);
                                    leaf.set_right2(null);

                                    leaf.get_parent().set_data2(leaf.get_parent().get_data1());
                                    leaf.get_parent().set_data1(middle);
                                    leaf.get_parent().set_isThreeNode(true);
                                    leaf.get_parent().set_right2(leaf.get_parent().get_right1());
                                    leaf.get_parent().set_right1(newNode);
                                    leaf.get_parent().set_left2(newNode);

                                    leaf.set_data2(null);
                                    leaf.set_data1(min);
                                    leaf.set_isThreeNode(false);
                                    return true;
                                }else{
                                    //ak je pravy potomok
                                    BST23Node newNode = new BST23Node(min);
                                    newNode.set_parent(leaf.get_parent());
                                    newNode.set_left1(leaf.get_left1());
                                    newNode.set_right1(leaf.get_right1());
                                    if (leaf.get_left1() != null && leaf.get_right1() != null){
                                        leaf.get_left1().set_parent(newNode);
                                        leaf.get_right1().set_parent(newNode);
                                    }

                                    leaf.get_parent().set_data2(middle);
                                    leaf.get_parent().set_isThreeNode(true);
                                    leaf.get_parent().set_right2(leaf.get_parent().get_right1());
                                    leaf.get_parent().set_right1(newNode);
                                    leaf.get_parent().set_left2(newNode);
                                    leaf.set_left1(leaf.get_left2());
                                    leaf.set_right1(leaf.get_right2());
                                    leaf.set_left2(null);
                                    leaf.set_right2(null);

                                    leaf.set_data2(null);
                                    leaf.set_data1(max);
                                    leaf.set_isThreeNode(false);
                                    return true;
                                }
                            }else {
                                //pokial je otec trojvrchol(doslo by k preteceniu)
                                if(leaf.get_parent().get_right2() == leaf){
                                    //leaf je pravy potomok
                                    BST23Node newNode = new BST23Node(min);
                                    newNode.set_parent(leaf.get_parent());
                                    newNode.set_left1(leaf.get_left1());
                                    newNode.set_right1(leaf.get_right1());
                                    if (leaf.get_left1() != null && leaf.get_right1() != null){
                                        leaf.get_left1().set_parent(newNode);
                                        leaf.get_right1().set_parent(newNode);
                                    }

                                    leaf.get_parent().set_left2(newNode);
                                    leaf.set_left1(leaf.get_left2());
                                    leaf.set_right1(leaf.get_right2());
                                    leaf.set_left2(null);
                                    leaf.set_right2(null);

                                    leaf.set_data2(null);
                                    leaf.set_data1(max);
                                    leaf.set_isThreeNode(false);

                                    leaf = leaf.get_parent();
                                    pNode.set_data1(middle);
                                }else if(leaf.get_parent().get_right1() == leaf){
                                    //leaf je v strede
                                    BST23Node newNode = new BST23Node(max);
                                    newNode.set_parent(leaf.get_parent());
                                    newNode.set_left1(leaf.get_left2());
                                    newNode.set_right1(leaf.get_right2());
                                    if (leaf.get_left2() != null && leaf.get_right2() != null){
                                        leaf.get_left2().set_parent(newNode);
                                        leaf.get_right2().set_parent(newNode);
                                    }
                                    leaf.set_left2(null);
                                    leaf.set_right2(null);

                                    leaf.get_parent().set_left2(newNode);
                                    leaf.set_data2(null);
                                    leaf.set_data1(min);
                                    leaf.set_isThreeNode(false);

                                    leaf = leaf.get_parent();
                                    pNode.set_data1(middle);
                                }else {
                                    //leaf je lavy potomok
                                    BST23Node newNode = new BST23Node(max);
                                    newNode.set_parent(leaf.get_parent());
                                    newNode.set_left1(leaf.get_left2());
                                    newNode.set_right1(leaf.get_right2());
                                    if (leaf.get_left2() != null && leaf.get_right2() != null){
                                        leaf.get_left2().set_parent(newNode);
                                        leaf.get_right2().set_parent(newNode);
                                    }
                                    leaf.set_left2(null);
                                    leaf.set_right2(null);

                                    leaf.get_parent().set_right1(newNode);
                                    leaf.set_data2(null);
                                    leaf.set_data1(min);
                                    leaf.set_isThreeNode(false);

                                    leaf = leaf.get_parent();
                                    pNode.set_data1(middle);
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean areNodesEqual(BST23Node node1, BST23Node node2){
        if (node1.get_left1() != node2.get_left1()){
            return false;
        }
        if (node1.get_right1() != node2.get_right1()){
            return false;
        }
        if (node1.get_left2() != node2.get_left2()){
            return false;
        }
        if (node1.get_right2() != node2.get_right2()){
            return false;
        }
        if (node1.get_parent() != node2.get_parent()){
            return false;
        }
        if (node1.isThreeNode() != node2.isThreeNode()){
            return false;
        }
        if ((node1.get_data1() != null) && (node2.get_data1() != null)){
            if (node1.get_data1().compareTo(node2.get_data1()) != 0){
                return false;
            }
        }else {
            return false;
        }
        if ((node1.get_data2() != null) && (node2.get_data2() != null)){
            if (node1.get_data2().compareTo(node2.get_data2()) != 0){
                return false;
            }
        }else {
            if (node1.get_data2() != null){
                return false;
            }else if (node2.get_data2() != null){
                return false;
            }
        }
        return true;
    }

    private T getMin(BST23Node threeNode, BST23Node addedNode){
        T min;
        if (threeNode.get_data1().compareTo(threeNode.get_data2()) > 0){
            //overit spravnost pretypovania
            min = (T) threeNode.get_data1();
        }else {
            min = (T) threeNode.get_data2();
        }
        if (addedNode.get_data1().compareTo(min) > 0){
            min = (T) addedNode.get_data1();
        }
        return min;
    }

    private T getMax(BST23Node threeNode, BST23Node addedNode){
        T max;
        if (threeNode.get_data1().compareTo(threeNode.get_data2()) < 0){
            //overit spravnost pretypovania
            max = (T) threeNode.get_data1();
        }else {
            max = (T) threeNode.get_data2();
        }
        if (addedNode.get_data1().compareTo(max) < 0){
            max = (T) addedNode.get_data1();
        }
        return max;
    }

    private T getMiddle(BST23Node threeNode, BST23Node addedNode){
        T smaller;
        T bigger;
        if (threeNode.get_data1().compareTo(threeNode.get_data2()) > 0){
            //overit spravnost pretypovania
            smaller = (T) threeNode.get_data1();
            bigger = (T) threeNode.get_data2();
        }else {
            smaller = (T) threeNode.get_data2();
            bigger = (T) threeNode.get_data1();
        }
        if (addedNode.get_data1().compareTo(smaller) < 0){
            if(addedNode.get_data1().compareTo(bigger) > 0){
                return (T) addedNode.get_data1();
            }else {
                return bigger;
            }
        }else {
            return smaller;
        }
    }

    private BST23Node findLeafForInsert(BST23Node pNode){
        if (_root != null){
            //if (_root.get_left() == null && _root.get_right() == null){
            if (_root.get_left1() == null &&
                    _root.get_right1() == null &&
                    _root.get_right2() == null){
                //nema synov
                //overovanie ci 2 vrchol alebo 3 sa bude robit az v insert
                return _root;
            }else {
                BST23Node prev = null;
                BST23Node temp = _root;
                while (temp != null){
                    //if (temp.get_left() == null && temp.get_right() == null){
                    if (temp.get_left1() == null &&
                            temp.get_right1() == null &&
                            temp.get_right2() == null){
                        return temp;
                    }else {
                        if (temp.isThreeNode()){
                            //v pnode je zase data len data1
                            if(temp.get_data1().compareTo(pNode.get_data1()) < 0){
                                prev = temp;
                                //temp = prev.get_left();
                                temp = prev.get_left1();
                            }else if(temp.get_data2().compareTo(pNode.get_data1()) > 0){
                                prev = temp;
                                //temp = prev.get_right();
                                temp = prev.get_right2();
                            }else if ((temp.get_data1().compareTo(pNode.get_data1()) > 0) &&
                                    (temp.get_data2().compareTo(pNode.get_data1()) < 0)){
                                prev = temp;
                                //temp = prev.get_middle();
                                temp = prev.get_right1();
                            }
                        }else {
                            if (temp.get_data1().compareTo(pNode.get_data1()) < 0){
                                prev = temp;
                                //temp = prev.get_left();
                                temp = prev.get_left1();
                            }else if (temp.get_data1().compareTo(pNode.get_data1()) > 0){
                                prev = temp;
                                //temp = prev.get_right();
                                temp = prev.get_right1();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public BST23Node find(BST23Node pNode){
        //pomocne vytvoreny pNode kty je posielany ako parameter bude mat hladany kluc v data1
        if (_root == null ||
                (pNode.get_data1().compareTo(_root.get_data1()) == 0) ||
                ((_root.get_data2() != null) && (pNode.get_data1().compareTo(_root.get_data2()) == 0))){
            return _root;
        }else {
            BST23Node prev = null;
            BST23Node temp = _root;
            while (temp != null){
                if (temp.isThreeNode()){
                    if (temp.get_data1().compareTo(pNode.get_data1()) < 0){
                        //hladany kluc je mensi ako lavy vrchol(teda data1)
                        prev = temp;
                        //temp = prev.get_left();
                        temp = prev.get_left1();
                    }else if (temp.get_data2().compareTo(pNode.get_data1()) > 0){
                        //hladany kluc je vacsi ako pravy vrchol
                        prev = temp;
                        //temp = prev.get_right();
                        temp = prev.get_right2();
                    }else if ((temp.get_data1().compareTo(pNode.get_data1()) > 0) &&
                            (temp.get_data2().compareTo(pNode.get_data1()) < 0)){
                        //hladany kluc je medzi pravym a lavym klucom
                        prev = temp;
                        //temp = prev.get_middle();
                        temp = prev.get_right1();
                    }else if ((temp.get_data1().compareTo(pNode.get_data1()) == 0) ||
                            (temp.get_data2().compareTo(pNode.get_data1()) == 0)){
                        //hladany kluc je jeden z klucov dvoch vrcholov
                        return temp;
                    }
                }else {
                    if (temp.get_data1().compareTo(pNode.get_data1()) > 0){
                        //ak pnode data1 je vacsie ako tempdata
                        prev = temp;
                        //temp = prev.get_right();
                        temp = prev.get_right1();
                    }else if(temp.get_data1().compareTo(pNode.get_data1()) < 0){
                        //ak pnode data1 je mensie ako tempdata
                        prev = temp;
                        //temp = prev.get_left();
                        temp = prev.get_left1();
                    }
                    if (temp != null){
                        if (temp.get_data1().compareTo(pNode.get_data1()) == 0){
                            return temp;
                        }
                    }
                }
            }
            return null;
        }
    }
}
