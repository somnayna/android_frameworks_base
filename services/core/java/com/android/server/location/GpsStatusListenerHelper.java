 +    public void onGnssSvStatusChanged(
+            final int svCount,
+            final int[] prns,
+            final float[] snrs,
+            final float[] elevations,
+            final float[] azimuths,
+            final boolean[] ephemerisPresences,
+            final boolean[] almanacPresences,
+            final boolean[] usedInFix) {
+        Operation operation = new Operation() {
+            @Override
+            public void execute(IGpsStatusListener listener) throws RemoteException {
+                listener.onGnssSvStatusChanged(
+                        svCount,
+                        prns,
+                        snrs,
+                        elevations,
+                        azimuths,
+                        ephemerisPresences,
+                        almanacPresences,
+                        usedInFix);
+            }
+        };
+        foreach(operation);
+    }
+
