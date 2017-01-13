package io.swagger.utils;

/**
 * Created by durza9390 on 13.12.2016.
 */
public class PairEqualsLeft<L,R> {

    private final L left;
    private final R right;

    public PairEqualsLeft(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PairEqualsLeft)) return false;
        PairEqualsLeft pairo = (PairEqualsLeft) o;
        return this.left.equals(pairo.getLeft());
    }

}