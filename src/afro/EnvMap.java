/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package afro;

import java.util.Map;

/**
 *
 * @author dmn
 */
public class EnvMap {
    public static void main (String[] args) {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }
    }
}