package com.bobmowzie.mowziesmobs.common.gen.structure;

/**
 * Created by jnad325 on 6/24/15.
 */
public class StructureWroughtnautRoom {
    public static final int[][][][] blockArray =
            {
                    { // y
                            { // x
                                    // z: place 5 blocks here that will run north to south
                                    // This first z column is stored at x = 0 in our block array
                                    {1}, // here's one, be sure to add a comma ','
                                    {1}, // 2
                                    {1}, // 3
                                    {1}, // 4
                                    {1} // 5
                                    // That finishes one column, but we need four more columns
                                    // The next column will be at x = 1
                            }, // Again, be sure to add a comma ','
                            { // This is the start of x = 1
                                    // The brackets within start off the z array once again
                                    // Just copy and paste from above!
                                    {1}, // here's one, be sure to add a comma ','
                                    {1}, // 2
                                    {1}, // 3
                                    {1}, // 4
                                    {1} // 5
                                    // Just like that! Now copy an entire x subsection and paste it
                                    // 3 more times. Don't forget to add that comma!
                            },
                            { // This is the start of x = 1
                                    // The brackets within start off the z array once again
                                    // Just copy and paste from above!
                                    {1}, // here's one, be sure to add a comma ','
                                    {1}, // 2
                                    {1}, // 3
                                    {1}, // 4
                                    {1} // 5
                                    // Just like that! Now copy an entire x subsection and paste it
                                    // 3 more times. Don't forget to add that comma!
                            },
                            { // This is the start of x = 1
                                    // The brackets within start off the z array once again
                                    // Just copy and paste from above!
                                    {1}, // here's one, be sure to add a comma ','
                                    {1}, // 2
                                    {1}, // 3
                                    {1}, // 4
                                    {1} // 5
                                    // Just like that! Now copy an entire x subsection and paste it
                                    // 3 more times. Don't forget to add that comma!
                            },
                            { // This is the start of x = 1
                                    // The brackets within start off the z array once again
                                    // Just copy and paste from above!
                                    {1}, // here's one, be sure to add a comma ','
                                    {1}, // 2
                                    {1}, // 3
                                    {1}, // 4
                                    {1} // 5 <-- Remove the last comma!
                                    // Just like that! Now copy an entire x subsection and paste it
                                    // 3 more times. Don't forget to add that comma!
                            }, // <-- Remove the last comma, as there won't be another x sub-array here
                    }
            };
}
