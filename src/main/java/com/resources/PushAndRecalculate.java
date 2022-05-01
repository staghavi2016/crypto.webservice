package com.resources;


import com.api.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Map;


@Path("")
public class PushAndRecalculate {
    private StreamDataStats stat;

    public PushAndRecalculate(StreamDataStats stat) {
        this.stat = stat;
    }


     /* Supports calculating streaming (i.e., moving) mean and standard deviation of streaming input single integers.
     ** Input format is: '{"signleNum":SINGLE INTEGER NUMBER}'
     **
     ** e.g.,
     **  curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d
     **  '{"signleNum":2}'  http://0.0.0.0:8080/PushAndRecalculate
     **
     **   OUTPUTS: {"mean":8.0,"standardD":1.0}
     */
    @POST
    @Path("/PushAndRecalculate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StatsMap PushAndRecalculate(@NotNull @Valid IntNumber singleNum) {
        Map<String, Float> res =  stat.getMovingStats(singleNum.getNum());
        float mean = res.get("mean");
        float standardD = res.get("standardD");
        return new StatsMap(mean, standardD);
    }

}
