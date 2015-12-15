import org.kiwi.crius.tools.SqlDataType;
import org.kiwi.crius.tools.modle.ImportContextImpl;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-7
 * Time: 下午12:02
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        String s = SqlDataType.forType("CLOB");
        String DOUBLE = SqlDataType.forType("BLOB");
        System.out.println(s);
        ImportContextImpl  importContext = new ImportContextImpl("cn.damai.crius.tools.mode");
        String fffff = importContext.importType(s);
        String fffffs = importContext.importType(DOUBLE);
        System.out.println(fffff);
        System.out.println(fffffs);
        String ddd = importContext.generateImports();

        System.out.println("import---------------\n"+ddd);
    }
}
