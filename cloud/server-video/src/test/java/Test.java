import cn.hutool.crypto.digest.DigestUtil;

/**
 * @desc
 * @date 2020-04-17
 */
public class Test {
    public static void main(String[] args) {
        String s = DigestUtil.md5Hex("BR4XPMAY2X:01E4ATDERV2SW0AV");
        System.out.println(s);
    }
}
