package Model;

public enum MenuOption {
    START,
    OPTION,
    CLOSE,
    CHARACTER,
    BACK,
    CLOSED;

    public int returnvalue(){
        switch (this){
            case START:
                return 0;
            case OPTION:
                return 1;
            case CLOSE:
                return 2;
            case CHARACTER:
                return 0;
            case BACK:
                return 1;
            default:
                return 3;
        }
    }

    public MenuOption getByvalue(int i,boolean inOptions){
        // +4 since negative -i will produce -1 in some cases

        if (!inOptions) {
            i += 3;
            i = i % 3;

            switch (i){
                case 0:
                    return START;
                case 1:
                    return OPTION;
                case 2:
                    return CLOSE;
                default:
                    return CLOSED;
            }
        }else{
            i += 2;
            i%=2;

            switch (i){
                case 0:
                    return CHARACTER;
                case 1:
                    return BACK;
                default:
                    return START;
            }
        }
    }
}
