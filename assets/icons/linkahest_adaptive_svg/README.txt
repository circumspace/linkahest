Linkahest icon sources

Standalone app icon (rounded tile):
- ../linkahest_app_icon.svg
- Rendered to app/src/main/res/drawable-nodpi/app_icon.png (512px)
- Used by the README and the in-app logo image.

Render with a standards-compliant SVG engine (WebKit/QuickLook, rsvg, Inkscape,
or a browser). The flask outline is a stroked path and some naive rasterizers
(notably ImageMagick's built-in MSVG) drop it, leaving only the liquid.

Adaptive launcher layers (Android applies its own mask, so the background is
intentionally full-bleed and must not be rounded):
- drawable/ic_linkahest_foreground.svg
- drawable/ic_linkahest_monochrome.svg
- drawable/ic_linkahest_background.svg
- drawable/ic_linkahest_background_dark.svg

Adaptive resources:
- mipmap-anydpi-v26/ic_launcher.xml and ic_launcher_round.xml (background + foreground)
- mipmap-anydpi-v33/ic_launcher.xml and ic_launcher_round.xml (adds monochrome)

The foreground flask is intentionally smaller and centered.
Light background: #7C3AED
Dark background: #121212
Copper stroke: #D89B6B
Liquid purple: #A855F7
