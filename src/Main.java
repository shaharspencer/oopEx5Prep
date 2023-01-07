//lass LeafMovement {
//private final GameObject leaf;
//private final Random random = new Random();
//private final Vector2 initialLeafLocation;
//private final float initialOpaqueness;
//private Transition<Float> horizontalTransition;
//
//public LeafMovement(GameObject leaf) {
//        this.leaf = leaf;
//        this.initialLeafLocation = leaf.getCenter();
//        this.initialOpaqueness = leaf.renderer().getOpaqueness();
//        initializeMovement();
//        initiateLifeCycle();
//        }
//
//private void initializeMovement() {
//        float waitTime = (float) Math.random();
//        new ScheduledTask(leaf, waitTime, false, this::addTransitions);
//        }
//
//private void initiateLifeCycle() {
//        float waitTime = random.nextInt(20) + 5;
//        new ScheduledTask(leaf, waitTime, false, this::fadeOut);
//        }
//
//private void fadeOut() {
//        leaf.renderer().fadeOut(7, this::deathState);
//        leaf.transform().setVelocityY(60);
//        horizontalTransition = new Transition<>(leaf, (speed) -> leaf.transform().setVelocityX((Float) speed),
//        10f, -10f, Transition.CUBIC_INTERPOLATOR_FLOAT,
//        2, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
//        null);
//        }
//
//private void deathState() {
//        float waitTime = random.nextInt(3);
//        new ScheduledTask(leaf, waitTime, false, this::initializeLeafState);
//        }
//
//private void initializeLeafState() {
//        this.leaf.setCenter(this.initialLeafLocation);
//        this.leaf.renderer().setOpaqueness(this.initialOpaqueness);
//        this.leaf.setVelocity(Vector2.ZERO);
//        leaf.removeComponent(horizontalTransition);
//        initiateLifeCycle();
//        }
//
//private void addTransitions() {
//        int firstSign;
//        if (random.nextBoolean()) {
//        firstSign = 1;
//        } else {
//        firstSign = -1;
//        }
//        new Transition<>(leaf, (angle) -> leaf.renderer().setRenderableAngle((Float) angle),
//        firstSign * 10f,
//        -1 * firstSign * 10f, Transition.CUBIC_INTERPOLATOR_FLOAT,
//        2,
//        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
//        null);
//        new Transition<>(leaf, (x) -> leaf.setDimensions(Vector2.ONES.mult((Float) x)), (float) Block.SIZE,
//        Block.SIZE * 0.8f, Transition.CUBIC_INTERPOLATOR_FLOAT,
//        1,
//        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
//        null);
//        }
//        }
