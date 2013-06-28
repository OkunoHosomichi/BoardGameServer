package gmi.utils;

/**
 * int型の範囲を表現する不変クラスです。最小値と最大値を保持し、最小値以上で最大値以下ならば範囲内であると判定します。
 * 最小値と最大値が同じ値の場合もありえます。
 * 
 * @author おくのほそみち
 */
public final class IntRange {
  private final int fMax;
  private final int fMin;

  /**
   * 最小値と最大値を指定して新しいインスタンスを作成します。 もしも最大値より最小値の方が大きかったら入れ替えて作成します。
   * 
   * @param min
   *          最小値。
   * @param max
   *          最大値。
   */
  public IntRange(int min, int max) {

    if (max >= min) {
      fMin = min;
      fMax = max;
    } else {
      fMin = max;
      fMax = min;
    }
  }

  /**
   * 指定された整数値が最小値以上で最大値以下の範囲にあるか調べます。
   * 
   * @param number
   *          値
   * @return numberが範囲内ならtrue、範囲外ならfalse。
   */
  public boolean Contains(int number) {

    return fMin <= number && number <= fMax;
  }

  /*
   * Eclipseにより自動生成しました。
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final IntRange other = (IntRange) obj;
    if (fMax != other.fMax) return false;
    if (fMin != other.fMin) return false;
    return true;
  }

  /*
   * Eclipseにより自動生成しました。
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fMax;
    result = prime * result + fMin;
    return result;
  }

  /**
   * 最大値を返します。最小値以上です。この値を超えたら範囲外です。
   * 
   * @return 最大値。
   */
  public int max() {
    assert fMax >= fMin;

    return fMax;
  }

  /**
   * 最小値を返します。最大値以下です。この値未満なら範囲外です。
   * 
   * @return 最小値。
   */
  public int min() {
    assert fMax >= fMin;

    return fMin;
  }

  @Override
  public String toString() {

    return Integer.toString(fMin) + "～" + Integer.toString(fMax);
  }
}