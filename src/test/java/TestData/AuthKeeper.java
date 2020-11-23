package TestData;

import static TestData.Constants.AuthKeys.*;

public class AuthKeeper {
    public String getUnionLogin(){
        return System.getProperty(UNION_LOGIN);
    }

    public String getUnionPassword(){
        return System.getProperty(UNION_PASSWORD);
    }

    public String getTrLogin(){
        return System.getProperty(TR_LOGIN);
    }

    public String getTrPassword(){
        return System.getProperty(TR_PASSWORD);
    }
}
