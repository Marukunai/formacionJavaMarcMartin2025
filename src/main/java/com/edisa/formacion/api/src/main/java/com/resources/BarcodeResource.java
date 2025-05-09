package com.edisa.formacion.api.src.main.java.com.resources;

import com.edisa.formacion.api.src.main.java.com.utils.BarcodeUtil;
import com.google.zxing.BarcodeFormat;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

@Path("/barcode")
@Produces(MediaType.APPLICATION_JSON)
public class BarcodeResource {

    @GET
    @Path("/generate")
    public Response generateCode(
            @QueryParam("text") String text,
            @QueryParam("format") String formatStr,
            @QueryParam("path") String outputPath
    ) {
        try {
            BarcodeFormat format = BarcodeFormat.valueOf(formatStr.toUpperCase());
            BarcodeUtil.generateBarcodeImage(text, format, outputPath);
            return Response.ok().entity("{\"status\":\"OK\",\"path\":\"" + outputPath + "\"}").build();
        } catch (Exception e) {
            return Response.serverError().entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/detect")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response detectCode(@FormParam("imagePath") String imagePath) {
        try {
            List<String> codes = BarcodeUtil.readBarcodesFromImage(new File(imagePath));
            return Response.ok(codes).build();
        } catch (Exception e) {
            return Response.serverError().entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
