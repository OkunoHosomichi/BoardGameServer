package gmi.boardgame.validator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import static gmi.utils.Preconditions.checkNotEmptyArgument;
import static gmi.utils.Preconditions.checkNotNullArgument;

/**
 * ニックネームの妥当性を検査するクラスです。
 * 
 * @author おくのほそみち
 */
public final class NicknameValidator {
  /**
   * ニックネームに含むことができない文字のパターン。
   */
  private static final Pattern ILLEGAL_CHARACTER_PATTERN = Pattern.compile("[, @]");
  /**
   * 予約されたニックネームの集合。変更できません。
   */
  private static final Set<String> RESERVED;
  static {
    final Set<String> result = new HashSet<>();
    result.add("SERVER");
    result.add("SYSTEM");
    result.add("ADMIN");

    RESERVED = Collections.unmodifiableSet(result);
  }

  /**
   * 指定されたニックネームが妥当なものかどうか調べます。妥当な名前は以下の条件を全て満たしています。<br>
   * ･予約されたニックネームではない。<br>
   * ･半角のカンマ、スペース、アットマークなど不正な文字が含まれていない。
   * 
   * @param nickname
   *          調べるニックネーム。null又は空文字列を指定できません。
   * @return 妥当な名前でなければtrue、不正ならばfalse。
   * @throws IllegalArgumentException
   *           nicknameがnull又は空文字列の場合。
   */
  public static final boolean isValid(@Nonnull String nickname) {
    checkNotNullArgument(nickname, "nickname");
    checkNotEmptyArgument(nickname, "nickname");

    return !(isReserved(nickname) || containsIllegalCharacter(nickname));
  }

  /**
   * 指定されたニックネーム内に不正な文字が含まれているかどうかを返します。
   * 
   * @param nickname
   *          ニックネーム。null又は空文字列を指定できません。
   * @return 不正な文字が含まれていればtrue、含まれていなければfalse。
   */
  private static final boolean containsIllegalCharacter(@Nonnull String nickname) {
    assert nickname != null;
    assert !nickname.isEmpty();

    return ILLEGAL_CHARACTER_PATTERN.matcher(nickname).find();
  }

  /**
   * 指定されたニックネームが予約されたものと等しいかどうかを返します。比較する時に大文字と小文字の区別を行いません。
   * 
   * @param nickname
   *          ニックネーム。null又は空文字列を指定できません。
   * @return 予約されていればtrue、予約されていなければfalse。
   */
  private static final boolean isReserved(@Nonnull String nickname) {
    assert nickname != null;
    assert !nickname.isEmpty();

    return RESERVED.contains(nickname.toUpperCase(Locale.ENGLISH));
  }
}
