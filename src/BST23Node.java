public class BST23Node<T extends Comparable<T>> {
    private BST23Node _left1;
    private BST23Node _right1;
    private BST23Node _left2;
    private BST23Node _right2;
    private BST23Node _parent;
    private T _data1;
    private T _data2;
    private boolean _isThreeNode;

    public BST23Node(T pData1){
        _data1 = pData1;
        _data2 = null;
        _isThreeNode = false;
        _left1 = null;
        _right1 = null;
        _left2 = null;
        _right2 = null;
    }

    /*public BST23Node(BST23Node pLeft,
                     BST23Node pRight,
                     BST23Node pMiddle,
                     BST23Node pParent,
                     T pData1,
                     T pData2,
                     boolean pIsThreeNode){
        _left = pLeft;
        _right = pRight;
        _middle = pMiddle;
        _parent = pParent;
        _data1 = pData1;
        _data2 = pData2;
        _isThreeNode = pIsThreeNode;
    }*/

    public BST23Node get_parent() {
        return _parent;
    }

    public void set_parent(BST23Node _parent) {
        this._parent = _parent;
    }

    public T get_data1() {
        return _data1;
    }

    public void set_data1(T _data1) {
        this._data1 = _data1;
    }

    public T get_data2() {
        return _data2;
    }

    public void set_data2(T _data2) {
        this._data2 = _data2;
    }

    public boolean isThreeNode() {
        return _isThreeNode;
    }

    public void set_isThreeNode(boolean _isThreeNode) {
        this._isThreeNode = _isThreeNode;
    }

    public BST23Node get_left1() {
        return _left1;
    }

    public void set_left1(BST23Node _left1) {
        this._left1 = _left1;
    }

    public BST23Node get_right1() {
        return _right1;
    }

    public void set_right1(BST23Node _right1) {
        this._right1 = _right1;
    }

    public BST23Node get_left2() {
        return _left2;
    }

    public void set_left2(BST23Node _left2) {
        this._left2 = _left2;
    }

    public BST23Node get_right2() {
        return _right2;
    }

    public void set_right2(BST23Node _right2) {
        this._right2 = _right2;
    }
}
