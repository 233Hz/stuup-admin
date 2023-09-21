package com.poho.stuup.constant;

public class ValidationGroups {

    public interface Get {
    }

    public interface ADD {
    }

    public interface Update {
    }

    public interface Delete {
    }

    public interface Audit {

        interface Single {
            interface Pass {
            }

            interface NoPass {
            }
        }

        interface Batch {
            interface Pass {
            }

            interface NoPass {
            }
        }

    }

}
