package model.adt;

public class Tuple<V1, V2, V3> {
  private V1 v1;
  private V2 v2;
  private V3 v3;

  public Tuple(V1 n1, V2 l1, V3 n2) {
    this.v1 = n1;
    this.v2 = l1;
    this.v3 = n2;
  }

  public V1 getV1() {
    return this.v1;
  }

  public V2 getV2() {
    return this.v2;
  }

  public V3 getV3() {
    return this.v3;
  }

  public void setV1(V1 v) {
    this.v1 = v;
  }

  public void setV2(V2 v) {
    this.v2 = v;
  }

  public void setV3(V3 v) {
    this.v3 = v;
  }

  @Override
  public String toString() {
    return "(" + this.v1 + "," + this.v2 + "," + this.v3 + ")";
  }
}
