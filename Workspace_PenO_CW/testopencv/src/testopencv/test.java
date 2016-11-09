/*
 * mogelijke manier omzetten array pixels to buffered image
 * 
 * BufferedImage img=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
for(int r=0; r<height; r++)
for(int c=0; c<width; c++)
{
  int index=r*width+c;
  int red=colors[index] & 0xFF;
  int green=colors[index+1] & 0xFF;
  int blue=colors[index+2] & 0xFF;
  int rgb = (red << 16) | (green << 8) | blue;
  img.setRGB(c, r, rgb);
  
  
  of
  
  public static Image getImageFromArray(int[] pixels, int width, int height) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            WritableRaster raster = (WritableRaster) image.getData();
            raster.setPixels(0,0,width,height,pixels);
            return image;
        }
}
 */

/*
 * van buffered naar mat
 * public static Mat bufferedImageToMat(BufferedImage bi) {
  Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
  byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
  mat.put(0, 0, data);
  return mat;
}
 */


/*
 * naar hsv en morph operaties?
 * inrange voor color detection: inRange(Mat src, Scalar lowerb, Scalar upperb, Mat dst)
 * 
 * veranderen BGR -> RGB
 * The red rectangle has value (0,0,240), so you can use:
inRange(img, new Scalar(0, 0, 230), new Scalar(0, 0, 255), dst);

The green rectangle has value (0,240,0), so you can use:
inRange(img, new Scalar(0, 230, 0), new Scalar(0, 255, 0), dst);

The blue rectangle has value (240,0,0), so you can use:
inRange(img, new Scalar(230, 0, 0), new Scalar(255, 0, 0), dst);

The gray rectangle has value (100,100,100), so you can use:
inRange(img, new Scalar(90, 90, 90), new Scalar(110, 110, 110), dst);

 */

/*
 * vorm herkenning + zoek coordinaten
 */


